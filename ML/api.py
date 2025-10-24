from flask import Flask, request, jsonify
from flask_cors import CORS
from sentence_transformers import SentenceTransformer
import numpy as np
import joblib

app = Flask(__name__)
CORS(app)  # Enable CORS for all routes

model = SentenceTransformer("all-MiniLM-L6-v2")


# --- Utility ---
def clean_text(text):
    if not text:
        return ""
    return text.replace("\n", " ").strip().lower()

def cosine_similarity(vec1, vec2):
    if vec1 is None or vec2 is None:
        return 0.0
    dot = np.dot(vec1, vec2)
    norm1 = np.linalg.norm(vec1)
    norm2 = np.linalg.norm(vec2)
    return float(dot / (norm1 * norm2)) if norm1 > 0 and norm2 > 0 else 0.0


# --- Flask API ---
@app.route("/rank_applicants", methods=["POST"])
def rank_applicants():
    """
    Request JSON:
    {
        "job": {
            "title": "Software Engineer",
            "description": "...",
            "requirements": "..."
        },
        "applicants": [
            {
                "id": 1,
                "title": "Resume Title",
                "summary": "...",
                "skills": "...",
                "education": [{"degree": "BCA", "institution": "XYZ"}],
                "experience": [{"jobTitle": "Intern", "companyName": "ABC", "description": "Worked on ..."}],
                "certifications": [{"title": "AWS Certified", "org": "Amazon"}]
            }
        ]
    }
    """
    data = request.json
    job = data.get("job", {})
    applicants = data.get("applicants", [])

    # 1. Build job text
    job_text = clean_text(
        f"{job.get('title','')} {job.get('description','')} {job.get('requirements','')}"
    )

    # 2. Build applicant texts
    applicant_texts = []
    applicant_ids = []
    for applicant in applicants:
        parts = []
        parts.append(applicant.get("title", ""))
        parts.append(applicant.get("summary", ""))
        parts.append(applicant.get("skills", ""))

        for edu in applicant.get("education", []):
            parts.append(edu.get("degree", ""))
            parts.append(edu.get("institution", ""))

        for exp in applicant.get("experience", []):
            parts.append(exp.get("jobTitle", ""))
            parts.append(exp.get("companyName", ""))
            parts.append(exp.get("description", ""))

        for cert in applicant.get("certifications", []):
            parts.append(cert.get("title", ""))
            parts.append(cert.get("org", ""))

        applicant_texts.append(clean_text(" ".join(parts)))
        applicant_ids.append(applicant.get("id"))

    # 3. Encode in batch for efficiency
    embeddings = model.encode([job_text] + applicant_texts, show_progress_bar=False)
    job_vec = embeddings[0]
    applicant_vecs = embeddings[1:]

    # 4. Score applicants
    results = []
    for app_id, app_vec in zip(applicant_ids, applicant_vecs):
        score = cosine_similarity(job_vec, app_vec)
        results.append({"applicantId": app_id, "score": score})

    # 5. Sort by score
    results = sorted(results, key=lambda x: x["score"], reverse=True)

    return jsonify(results)


if __name__ == "__main__":
    app.run(debug=True)
