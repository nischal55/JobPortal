<template>
  <div class="resume-container max-w-3xl mx-auto bg-white p-8 text-gray-900 leading-relaxed shadow-sm">
    <!-- Header: Name & Title -->
    <header class="border-b pb-4 mb-6">
      <h1 class="text-4xl font-bold">{{ username }}</h1>
      <p class="text-gray-600 mt-1">
        {{ personalInfo.linkedIn || 'email@example.com' }} | 
        {{ personalInfo.contactNo || '+123456789' }} | 
        {{ personalInfo.address || 'City, Country' }}
      </p>
    </header>

    <!-- Summary / Objective -->
    <section class="mb-6">
      <h2 class="text-xl font-semibold border-b border-gray-300 pb-1 mb-2">Profile</h2>
      <p>{{ personalInfo.summary || 'Brief professional summary or career objective goes here...' }}</p>
    </section>

    <!-- Education -->
    <section class="mb-6">
      <h2 class="text-xl font-semibold border-b border-gray-300 pb-1 mb-2">Education</h2>
      <div v-for="(edu, index) in educationList" :key="index" class="mb-3">
        <p class="font-semibold">{{ edu.degree || 'Degree Name' }} — {{ edu.institution || 'Institution Name' }}</p>
        <p class="text-gray-600 text-sm">{{ edu.startYear || 'Start Year' }} – {{ edu.endYear || 'End Year' }}</p>
      </div>
    </section>

    <!-- Certifications -->
    <section class="mb-6">
      <h2 class="text-xl font-semibold border-b border-gray-300 pb-1 mb-2">Certifications</h2>
      <div v-for="(cert, index) in certificationList" :key="index" class="mb-3">
        <p class="font-semibold">{{ cert.certificationTitle || 'Certification Name' }}</p>
        <p class="text-gray-600 text-sm">{{ cert.issuingOrg || 'Issuing Organization' }} — {{ cert.issueDate || 'Date' }}</p>
      </div>
    </section>

    <!-- Experience -->
    <section class="mb-6">
      <h2 class="text-xl font-semibold border-b border-gray-300 pb-1 mb-2">Experience</h2>
      <div v-for="(exp, index) in experienceList" :key="index" class="mb-4">
        <p class="font-semibold text-gray-900">{{ exp.jobTitle || 'Job Title' }} — {{ exp.companyName || 'Company Name' }}</p>
        <p class="text-gray-600 text-sm">{{ exp.startDate || 'Start Date' }} – {{ exp.endDate || 'End Date' }}</p>
        <p class="mt-1">{{ exp.description || 'Brief description of responsibilities and achievements...' }}</p>
      </div>
    </section>

    <!-- Skills -->
    <section v-if="personalInfo.skills && personalInfo.skills.length" class="mb-6">
      <h2 class="text-xl font-semibold border-b border-gray-300 pb-1 mb-2">Skills</h2>
      <ul class="flex flex-wrap gap-3 text-gray-700">
        <li v-for="(skill, index) in personalInfo.skills" :key="index" class="px-2 py-1 border rounded text-sm">
          {{ skill }}
        </li>
      </ul>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import ApiService from '@/services/ApiService';

const username = localStorage.getItem('username');

const personalInfo = ref({});
const educationList = ref([{ degree: '', institution: '', startYear: null, endYear: null }]);
const certificationList = ref([{ certificationTitle: '', issuingOrg: '', issueDate: '' }]);
const experienceList = ref([{ jobTitle: '', companyName: '', startDate: '', endDate: '', description: '' }]);

onMounted(async () => {
  try {
    const userId = localStorage.getItem('user_id');
    if (!userId) return;

    const jobSeekerRes = await ApiService.get(`/jobseeker/findByUserId/${userId}`);
    if (jobSeekerRes.data) Object.assign(personalInfo.value, jobSeekerRes.data);

    // Convert skills string to array if necessary
    if (personalInfo.value.skills && typeof personalInfo.value.skills === 'string') {
      personalInfo.value.skills = personalInfo.value.skills.split(',').map(s => s.trim());
    }

    const resumeRes = await ApiService.get(`/resumeDetail/findByUserId/${userId}`);
    if (!resumeRes.data) return;
    const resumeId = resumeRes.data.id;

    const eduRes = await ApiService.get(`/education/findByResumeId/${resumeId}`);
    educationList.value = eduRes.data.length ? eduRes.data : [{ degree: '', institution: '', startYear: null, endYear: null }];

    const certRes = await ApiService.get(`/certification/findByResumeId/${resumeId}`);
    certificationList.value = certRes.data.length ? certRes.data : [{ certificationTitle: '', issuingOrg: '', issueDate: '' }];

    const expRes = await ApiService.get(`/resumeExperiences/findByResumeId/${resumeId}`);
    experienceList.value = expRes.data.length ? expRes.data : [{ jobTitle: '', companyName: '', startDate: '', endDate: '', description: '' }];
  } catch (err) {
    console.error("Failed to fetch resume:", err);
  }
});
</script>

<style scoped>
.resume-container {
  font-family: 'Times New Roman', serif;
}
h1 {
  line-height: 1.2;
}
h2 {
  line-height: 1.3;
}
</style>
