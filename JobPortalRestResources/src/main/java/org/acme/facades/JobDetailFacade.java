package org.acme.facades;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.acme.models.*;
import org.jsoup.Jsoup;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class JobDetailFacade extends AbstractFacade<JobDetail> {

    @Inject
    ResumeDetailFacade resumeDetailFacade;

    @Inject
    ResumeEducationFacade resumeEducationFacade;

    @Inject
    ResumeExperienceFacade resumeExperienceFacade;

    @Inject
    ResumeCertificationFacade resumeCertificationFacade;

    @Inject
    JobSeekerDetailFacade jobSeekerDetailFacade;

    @Inject
    EntityManager em;

    public JobDetailFacade(){
        super(JobDetail.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }


    public List<JobDetail> recommendJobsByResume(Long resumeId) {
        // 1. Fetch resume
        ResumeDetail resume = resumeDetailFacade.find(resumeId);
        if (resume == null) return new ArrayList<>();

        JobSeekerDetail seeker = jobSeekerDetailFacade.findByUserId(resume.getUser().getId());

        // 2. Build resume text
        StringBuilder resumeText = new StringBuilder();
        resumeText.append(resume.getTitle()).append(" ");
        if (seeker != null) {
            if (seeker.getSummary() != null) resumeText.append(seeker.getSummary()).append(" ");
            if (seeker.getSkills() != null) resumeText.append(seeker.getSkills()).append(" ");
        }

        // Education
        resumeEducationFacade.findResumeByResumeId(resumeId)
                .forEach(edu -> resumeText.append(edu.getDegree()).append(" ").append(edu.getInstitution()).append(" "));

        // Experience
        resumeExperienceFacade.findResumeByResumeId(resumeId)
                .forEach(exp -> {
                    resumeText.append(exp.getJobTitle()).append(" ").append(exp.getCompanyName()).append(" ");
                    if (exp.getDescription() != null) resumeText.append(exp.getDescription()).append(" ");
                });

        // Certifications
        resumeCertificationFacade.findResumeByResumeId(resumeId)
                .forEach(cert -> {
                    resumeText.append(cert.getCertificationTitle()).append(" ");
                    if (cert.getIssuingOrg() != null) resumeText.append(cert.getIssuingOrg()).append(" ");
                });

        String cleanResumeText = cleanHtml(resumeText.toString());
        cleanResumeText = cleanResumeText.toLowerCase();

        // 3. Fetch all jobs
        List<JobDetail> allJobs = findAll();

        // 4. Build corpus (resume + all jobs) for correct IDF
        List<String> corpus = new ArrayList<>();
        corpus.add(cleanResumeText);
        for (JobDetail job : allJobs) {
            String jobText = (job.getTitle() != null ? job.getTitle() : "") + " " +
                    cleanHtml(job.getDescription()) + " " +
                    cleanHtml(job.getRequirements());
            corpus.add(jobText);
        }

        // 5. Compute IDF
        Map<String, Double> idfMap = computeIDF(corpus);

        // 6. Compute resume TF-IDF and log
        Map<String, Double> resumeTFIDF = computeTFIDF(cleanResumeText, idfMap);
        logTopTerms("Resume", resumeTFIDF);

        // 7. Score jobs
        Map<JobDetail, Double> jobScores = new HashMap<>();
        for (JobDetail job : allJobs) {
            String jobText = (job.getTitle() != null ? job.getTitle() : "") + " " +
                    cleanHtml(job.getDescription()) + " " +
                    cleanHtml(job.getRequirements());

            Map<String, Double> jobTFIDF = computeTFIDF(jobText, idfMap);
            double score = cosineSimilarity(resumeTFIDF, jobTFIDF);
            jobScores.put(job, score);

            System.out.println("\nJob: " + job.getTitle() + " | Score: " + score);
            logTopTerms(job.getTitle(), jobTFIDF);
        }

        // 8. Return jobs sorted by score
        return jobScores.entrySet().stream()
                .sorted(Map.Entry.<JobDetail, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // --- Utility functions ---

    private String cleanHtml(String html) {
        if (html == null) return "";
        return Jsoup.parse(html).text();
    }

    private List<String> tokenize(String text) {
        if (text == null) return Collections.emptyList();
        text = text.toLowerCase().replaceAll("[^a-z0-9+.# ]", " ");
        return Arrays.stream(text.split("\\s+"))
                .filter(t -> !t.isEmpty())
                .collect(Collectors.toList());
    }

    private Map<String, Double> computeIDF(List<String> documents) {
        Map<String, Integer> docFreq = new HashMap<>();
        int totalDocs = documents.size();

        for (String doc : documents) {
            Set<String> uniqueTerms = new HashSet<>(tokenize(doc));
            for (String term : uniqueTerms) {
                docFreq.put(term, docFreq.getOrDefault(term, 0) + 1);
            }
        }

        Map<String, Double> idf = new HashMap<>();
        for (Map.Entry<String, Integer> entry : docFreq.entrySet()) {
            idf.put(entry.getKey(), Math.log((double) totalDocs / (1 + entry.getValue())));
        }
        return idf;
    }

    private Map<String, Double> computeTFIDF(String text, Map<String, Double> idfMap) {
        Map<String, Integer> tf = new HashMap<>();
        for (String token : tokenize(text)) {
            tf.put(token, tf.getOrDefault(token, 0) + 1);
        }

        Map<String, Double> tfidf = new HashMap<>();
        tf.forEach((term, count) -> tfidf.put(term, count * idfMap.getOrDefault(term, 0.0)));
        return tfidf;
    }

    private double cosineSimilarity(Map<String, Double> vec1, Map<String, Double> vec2) {
        if (vec1.isEmpty() || vec2.isEmpty()) return 0.0;

        double dotProduct = 0.0;
        double magnitude1 = 0.0;
        double magnitude2 = 0.0;

        // Use smaller vector for efficient dot product
        Map<String, Double> smaller = vec1.size() < vec2.size() ? vec1 : vec2;
        Map<String, Double> larger = vec1.size() < vec2.size() ? vec2 : vec1;

        for (String term : smaller.keySet()) {
            if (larger.containsKey(term)) {
                dotProduct += smaller.get(term) * larger.get(term);
            }
        }

        // Calculate full magnitudes separately
        for (double value : vec1.values()) {
            magnitude1 += value * value;
        }
        for (double value : vec2.values()) {
            magnitude2 += value * value;
        }

        double denominator = Math.sqrt(magnitude1) * Math.sqrt(magnitude2);
        return denominator == 0.0 ? 0.0 : dotProduct / denominator;
    }

    private void logTopTerms(String label, Map<String, Double> tfidf) {
        System.out.println("---- Top terms for " + label + " ----");
        tfidf.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(10)
                .forEach(e -> System.out.println(e.getKey() + " : " + e.getValue()));
    }

    public List<JobDetail> findByProviderId(Long providerId){
        try{
            TypedQuery<JobDetail> query = em.createQuery("\"Select c from JobDetail c where c.provider.id = :provider_id",JobDetail.class);
            query.setParameter("provider_id",providerId);
            return query.getResultList();
        }catch (Exception e){
            return null;
        }
    }
}
