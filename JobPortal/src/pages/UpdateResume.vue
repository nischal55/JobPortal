<template>
  <Navbar />
  <div class="card flex justify-center mt-16">
    <Stepper :value="activeStep" linear class="basis-[50rem]">
      <StepList>
        <Step value="1">Personal Detail</Step>
        <Step value="2">Education</Step>
        <Step value="3">Certification</Step>
        <Step value="4">Experience</Step>
      </StepList>

      <StepPanels>
        <!-- Step 1: Personal Detail -->
        <StepPanel v-slot="{ activateCallback }" value="1">
          <div class="flex flex-col gap-6">
            <div class="p-10 rounded-md shadow-sm bg-surface-50">
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label class="text-sm font-medium">Contact No <span class="text-red-500">*</span></label>
                  <InputText v-model="personalInfo.contactNo" class="w-full p-inputtext-sm h-11" placeholder="+977-9800000000" />
                </div>
                <div>
                  <label class="text-sm font-medium">LinkedIn</label>
                  <InputText v-model="personalInfo.linkedIn" class="w-full p-inputtext-sm h-11" placeholder="https://linkedin.com/in/…" />
                </div>
                <div class="md:col-span-2">
                  <label class="text-sm font-medium">Address <span class="text-red-500">*</span></label>
                  <InputText v-model="personalInfo.address" rows="2" class="w-full p-inputtext-sm h-11" placeholder="Kumaripati, Lalipur" />
                </div>
                <div class="md:col-span-2">
                  <label class="text-sm font-medium">Professional Summary <span class="text-red-500">*</span></label>
                  <InputText v-model="personalInfo.summary" rows="3" class="w-full p-inputtext-sm h-11" placeholder="Brief overview of your background…" />
                </div>
                <div class="md:col-span-2">
                  <label class="text-sm font-medium">Skills <span class="text-red-500">*</span></label>
                  <InputText v-model="personalInfo.skills" class="w-full p-inputtext-sm h-11" placeholder="e.g., Java, Spring Boot, Vue 3" />
                </div>
              </div>
            </div>
            <div class="flex pt-6 justify-end">
              <Button label="Next" icon="pi pi-arrow-right" @click="activateCallback('2')" />
            </div>
          </div>
        </StepPanel>

        <!-- Step 2: Education -->
        <StepPanel v-slot="{ activateCallback }" value="2">
          <div class="flex flex-col gap-6">
            <div v-for="(entry, index) in educationList" :key="index" class="p-10 rounded-md shadow-sm bg-surface-50">
              <div class="flex justify-between items-center mb-2">
                <h3 class="font-semibold text-base">Education {{ index + 1 }}</h3>
                <div>
                  <Button icon="pi pi-trash" severity="danger" rounded text size="small" v-if="educationList.length > 1" @click="removeEducation(index)" />
                  <Button icon="pi pi-plus" severity="success" rounded text size="small" @click="addEducation" />
                </div>
              </div>
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label class="text-sm font-medium">Degree <span class="text-red-500">*</span></label>
                  <InputText v-model="entry.degree" class="w-full p-inputtext-sm h-11" />
                </div>
                <div>
                  <label class="text-sm font-medium">Institution <span class="text-red-500">*</span></label>
                  <InputText v-model="entry.institution" class="w-full p-inputtext-sm h-11" />
                </div>
                <div>
                  <label class="text-sm font-medium">Start Year</label>
                  <InputNumber v-model="entry.startYear" :useGrouping="false" class="w-full" />
                </div>
                <div>
                  <label class="text-sm font-medium">End Year</label>
                  <InputNumber v-model="entry.endYear" :useGrouping="false" class="w-full" />
                </div>
              </div>
            </div>
            <div class="flex pt-6 justify-between">
              <Button label="Back" severity="secondary" icon="pi pi-arrow-left" @click="activateCallback('1')" />
              <Button label="Next" icon="pi pi-arrow-right" @click="activateCallback('3')" />
            </div>
          </div>
        </StepPanel>

        <!-- Step 3: Certification -->
        <StepPanel v-slot="{ activateCallback }" value="3">
          <div class="flex flex-col gap-6">
            <div v-for="(cert, index) in certificationList" :key="index" class="p-10 rounded-md shadow-sm bg-surface-50">
              <div class="flex justify-between items-center mb-2">
                <h3 class="font-semibold text-base">Certification {{ index + 1 }}</h3>
                <div>
                  <Button icon="pi pi-trash" severity="danger" rounded text size="small" v-if="certificationList.length > 1" @click="removeCertification(index)" />
                  <Button icon="pi pi-plus" severity="success" rounded text size="small" @click="addCertification" />
                </div>
              </div>
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label class="text-sm font-medium">Title <span class="text-red-500">*</span></label>
                  <InputText v-model="cert.certificationTitle" class="w-full p-inputtext-sm h-11" />
                </div>
                <div>
                  <label class="text-sm font-medium">Issuing Organization <span class="text-red-500">*</span></label>
                  <InputText v-model="cert.issuingOrg" class="w-full p-inputtext-sm h-11" />
                </div>
                <div class="md:col-span-2">
                  <label class="text-sm font-medium">Issue Date</label>
                  <InputText v-model="cert.issueDate" class="w-full p-inputtext-sm h-11" placeholder="YYYY-MM-DD" />
                </div>
              </div>
            </div>
            <div class="flex pt-6 justify-between">
              <Button label="Back" severity="secondary" icon="pi pi-arrow-left" @click="activateCallback('2')" />
              <Button label="Next" icon="pi pi-arrow-right" iconPos="right" @click="activateCallback('4')" />
            </div>
          </div>
        </StepPanel>

        <!-- Step 4: Experience -->
        <StepPanel v-slot="{ activateCallback }" value="4">
          <div class="flex flex-col gap-6">
            <div v-for="(exp, index) in experienceList" :key="index" class="p-10 rounded-md shadow-sm bg-surface-50">
              <div class="flex justify-between items-center mb-2">
                <h3 class="font-semibold text-base">Experience {{ index + 1 }}</h3>
                <div>
                  <Button icon="pi pi-trash" severity="danger" rounded text size="small" v-if="experienceList.length > 1" @click="removeExperience(index)" />
                  <Button icon="pi pi-plus" severity="success" rounded text size="small" @click="addExperience" />
                </div>
              </div>
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label class="text-sm font-medium">Job Title <span class="text-red-500">*</span></label>
                  <InputText v-model="exp.jobTitle" class="w-full p-inputtext-sm h-11" />
                </div>
                <div>
                  <label class="text-sm font-medium">Company Name <span class="text-red-500">*</span></label>
                  <InputText v-model="exp.companyName" class="w-full p-inputtext-sm h-11" />
                </div>
                <div>
                  <label class="text-sm font-medium">Start Date</label>
                  <InputText v-model="exp.startDate" class="w-full p-inputtext-sm h-11" placeholder="YYYY-MM-DD" />
                </div>
                <div>
                  <label class="text-sm font-medium">End Date</label>
                  <InputText v-model="exp.endDate" class="w-full p-inputtext-sm h-11" placeholder="YYYY-MM-DD" />
                </div>
                <div class="md:col-span-2">
                  <label class="text-sm font-medium">Description</label>
                  <InputText v-model="exp.description" class="w-full p-inputtext-sm h-11" />
                </div>
              </div>
            </div>
            <div class="flex pt-6 justify-between">
              <Button label="Back" severity="secondary" icon="pi pi-arrow-left" @click="activateCallback('3')" />
              <!-- <Button label="Update" icon="pi pi-check" iconPos="right" @click="updateResume" /> -->
            </div>
          </div>
        </StepPanel>
      </StepPanels>
    </Stepper>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import Navbar from '@/components/navbar.vue';
import ApiService from '@/services/ApiService';
import { Button, Step, StepList, StepPanel, StepPanels, Stepper, InputText, InputNumber } from 'primevue';

const activeStep = ref('1');

// Personal Info
const personalInfo = ref({
  contactNo: '',
  linkedIn: '',
  address: '',
  summary: '',
  skills: ''
});

// Education
const educationList = ref([]);
function addEducation() { educationList.value.push({ degree: '', institution: '', startYear: null, endYear: null }); }
function removeEducation(index) { educationList.value.splice(index, 1); }

// Certification
const certificationList = ref([]);
function addCertification() { certificationList.value.push({ certificationTitle: '', issuingOrg: '', issueDate: '' }); }
function removeCertification(index) { certificationList.value.splice(index, 1); }

// Experience
const experienceList = ref([]);
function addExperience() { experienceList.value.push({ jobTitle: '', companyName: '', startDate: '', endDate: '', description: '' }); }
function removeExperience(index) { experienceList.value.splice(index, 1); }

// Fetch existing resume
onMounted(async () => {
  try {
    const userId = localStorage.getItem('user_id');
    if (!userId) return;

    // Fetch personal info
    const jobSeekerRes = await ApiService.get(`/jobseeker/findByUserId/${userId}`);
    if (jobSeekerRes.data) Object.assign(personalInfo.value, jobSeekerRes.data);

    // Fetch resume detail
    const resumeRes = await ApiService.get(`/resumeDetail/findByUserId/${userId}`);
    if (!resumeRes.data) return;
    const resumeId = resumeRes.data.id;

    // Fetch education
    const eduRes = await ApiService.get(`/education/findByResumeId/${resumeId}`);
    educationList.value = eduRes.data.length ? eduRes.data : [{ degree: '', institution: '', startYear: null, endYear: null }];

    // Fetch certification
    const certRes = await ApiService.get(`/certification/findByResumeId/${resumeId}`);
    certificationList.value = certRes.data.length ? certRes.data : [{ certificationTitle: '', issuingOrg: '', issueDate: '' }];

    // Fetch experience
    const expRes = await ApiService.get(`/resumeExperiences/findByResumeId/${resumeId}`);
    experienceList.value = expRes.data.length ? expRes.data : [{ jobTitle: '', companyName: '', startDate: '', endDate: '', description: '' }];
  } catch (err) {
    console.error("Failed to fetch resume:", err);
  }
});

// Update resume
async function updateResume() {
  try {
    const userId = localStorage.getItem('user_id');
    if (!userId) return;

    const resumeRes = await ApiService.get(`/resumedetail/findByUserId/${userId}`);
    const resumeId = resumeRes.data.id;

    // Update personal info
    await ApiService.put(`/jobseeker/findByUserId/${userId}`, personalInfo.value);

    // Update education
    for (const edu of educationList.value) {
      if (edu.id) {
        await ApiService.put(`/education/findById/${edu.id}`, edu);
      } else {
        await ApiService.post('/education/create', { ...edu, resume: { id: resumeId } });
      }
    }

    // Update certification
    for (const cert of certificationList.value) {
      if (cert.id) {
        await ApiService.put(`/certification/findById/${cert.id}`, cert);
      } else {
        await ApiService.post('/certification/create', { ...cert, resume: { id: resumeId } });
      }
    }

    // Update experience
    for (const exp of experienceList.value) {
      if (exp.id) {
        await ApiService.put(`/experience/findById/${exp.id}`, exp);
      } else {
        await ApiService.post('/experience/create', { ...exp, resume: { id: resumeId } });
      }
    }

    alert('Resume updated successfully!');
  } catch (err) {
    console.error('Update failed:', err);
    alert('Failed to update resume. Please try again.');
  }
}
</script>
