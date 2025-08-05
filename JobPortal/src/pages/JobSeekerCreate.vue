<template>
  <Navbar />
  <div class="card flex justify-center mt-16">
    <Stepper value="1" linear class="basis-[50rem]">
      <StepList>
        <Step value="1">Personal Detail</Step>
        <Step value="2">Education</Step>
        <Step value="3">Certification</Step>
        <Step value="4">Experience</Step>
      </StepList>

      <StepPanels>
        <StepPanel v-slot="{ activateCallback }" value="1">
          <div class="flex flex-col gap-6">
            <div class="p-10 rounded-md shadow-sm bg-surface-50">
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label class="text-sm font-medium">Contact No <span class="text-red-500">*</span></label>
                  <InputText v-model="personalInfo.contactNo" class="w-full p-inputtext-sm h-11"
                    placeholder="+977-9800000000" />
                </div>
                <div>
                  <label class="text-sm font-medium">LinkedIn</label>
                  <InputText v-model="personalInfo.linkedIn" class="w-full p-inputtext-sm h-11"
                    placeholder="https://linkedin.com/in/…" />
                </div>
                <div class="md:col-span-2">
                  <label class="text-sm font-medium">Address <span class="text-red-500">*</span></label>
                  <InputText v-model="personalInfo.address" rows="2" class="w-full p-inputtext-sm h-11"
                    placeholder="Kumaripati, Lalipur" />
                </div>
                <div class="md:col-span-2">
                  <label class="text-sm font-medium">Professional Summary <span class="text-red-500">*</span></label>
                  <InputText v-model="personalInfo.summary" rows="3" class="w-full p-inputtext-sm h-11"
                    placeholder="Brief overview of your background…" />
                </div>
                <div class="md:col-span-2">
                  <label class="text-sm font-medium">Skills <span class="text-red-500">*</span></label>
                  <InputText v-model="personalInfo.skills" class="w-full p-inputtext-sm h-11"
                    placeholder="e.g., Java, Spring Boot, Vue 3" />
                  <!-- Swap for Chips if you’d like: <Chips v-model="personalInfo.skills" /> -->
                </div>
              </div>
            </div>

            <div class="flex pt-6 justify-end">
              <Button label="Next" icon="pi pi-arrow-right" @click="activateCallback('2')" />
            </div>
          </div>
        </StepPanel>

        <StepPanel v-slot="{ activateCallback }" value="2">
          <div class="flex flex-col gap-6">
            <div v-for="(entry, index) in educationList" :key="index" class="p-10 rounded-md shadow-sm bg-surface-50">
              <div class="flex justify-between items-center mb-2">
                <h3 class="font-semibold text-base">Education {{ index + 1 }}</h3>
                <div>
                  <Button icon="pi pi-trash" severity="danger" rounded text size="small" v-if="educationList.length > 1"
                    @click="removeEducation(index)" />

                  <Button icon="pi pi-plus" severity="success" rounded text size="small" @click="addEducation" />
                </div>
              </div>

              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label class="text-sm font-medium">Degree <span class="text-red-500">*</span></label>
                  <InputText v-model="entry.degree" class="w-full p-inputtext-sm h-11" placeholder="e.g., BSc in CS" />
                </div>
                <div>
                  <label class="text-sm font-medium">Institution <span class="text-red-500">*</span></label>
                  <InputText v-model="entry.institution" class="w-full p-inputtext-sm h-11" placeholder="e.g., TU" />
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

        <!-- Step 2: Certification -->
        <StepPanel v-slot="{ activateCallback }" value="3">
          <div class="flex flex-col gap-6">
            <div v-for="(cert, index) in certificationList" :key="index"
              class="p-10 rounded-md shadow-sm bg-surface-50">
              <div class="flex justify-between items-center mb-2">
                <h3 class="font-semibold text-base">Certification {{ index + 1 }}</h3>
                <div>
                  <Button icon="pi pi-trash" severity="danger" rounded text size="small"
                    v-if="certificationList.length > 1" @click="removeCertification(index)" />
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
                  <!-- You can use a datepicker instead of InputText -->
                </div>
              </div>
            </div>

            <div class="flex pt-6 justify-between">
              <Button label="Back" severity="secondary" icon="pi pi-arrow-left" @click="activateCallback('1')" />
              <Button label="Next" icon="pi pi-arrow-right" iconPos="right" @click="activateCallback('4')" />
            </div>
          </div>
        </StepPanel>

        <!-- Step 3: Experience -->
        <StepPanel v-slot="{ activateCallback }" value="4">
          <div class="flex flex-col gap-6">
            <div v-for="(exp, index) in experienceList" :key="index" class="p-10 rounded-md shadow-sm bg-surface-50">
              <div class="flex justify-between items-center mb-2">
                <h3 class="font-semibold text-base">Experience {{ index + 1 }}</h3>
                <div>
                  <Button icon="pi pi-trash" severity="danger" rounded text size="small"
                    v-if="experienceList.length > 1" @click="removeExperience(index)" />
                  <Button  icon="pi pi-plus" severity="success" rounded text size="small" @click="addExperience" />
                </div>

              </div>

              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label class="text-sm font-medium">Job Title <span class="text-red-500">*</span></label>
                  <InputText v-model="exp.jobTitle" class="w-full p-inputtext-sm h-11"
                    placeholder="e.g., Software Engineer" />
                </div>
                <div>
                  <label class="text-sm font-medium">Company Name <span class="text-red-500">*</span></label>
                  <InputText v-model="exp.companyName" class="w-full p-inputtext-sm h-11" placeholder="e.g., Google" />
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
                  <InputText v-model="exp.description" class="w-full p-inputtext-sm h-11"
                    placeholder="e.g., Worked on..." />
                </div>
              </div>
            </div>
            <div class="flex pt-6 justify-between">
              <Button label="Back" severity="secondary" icon="pi pi-arrow-left" @click="activateCallback('2')" />
              <Button label="Submit" icon="pi pi-check" iconPos="right" @click="submitResume"/>
            </div>
          </div>
        </StepPanel>

      </StepPanels>
    </Stepper>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import Navbar from '@/components/navbar.vue';
import ApiService from '@/services/ApiService';
import {
  Button,
  Step,
  StepList,
  StepPanel,
  StepPanels,
  Stepper,
  InputText,
  InputNumber,
} from 'primevue'; // Make sure InputTextarea is imported

// Active step control for Stepper
const activeStep = ref('1');

// Personal Info (Step 1)
const personalInfo = ref({
  contactNo: '',
  linkedIn: '',
  address: '',
  summary: '',
  skills: ''
});

// Education Entries (Step 2)
const educationList = ref([
  { degree: '', institution: '', startYear: null, endYear: null }
]);

function addEducation() {
  educationList.value.push({ degree: '', institution: '', startYear: null, endYear: null });
}

function removeEducation(index) {
  educationList.value.splice(index, 1);
}

// Certification Entries (Step 3)
const certificationList = ref([
  { certificationTitle: '', issuingOrg: '', issueDate: '' }
]);

function addCertification() {
  certificationList.value.push({ certificationTitle: '', issuingOrg: '', issueDate: '' });
}

function removeCertification(index) {
  certificationList.value.splice(index, 1);
}

// Experience Entries (Step 4)
const experienceList = ref([
  { jobTitle: '', companyName: '', startDate: '', endDate: '', description: '' }
]);

function addExperience() {
  experienceList.value.push({ jobTitle: '', companyName: '', startDate: '', endDate: '', description: '' });
}

function removeExperience(index) {
  experienceList.value.splice(index, 1);
}

async function submitResume() {
  try {
    // Step 1: Get user_id from localStorage
    const userId = localStorage.getItem('user_id');
    if (!userId) {
      alert("User ID not found. Please login again.");
      return;
    }

    // Step 2: Create ResumeDetail first
    const resumeDetailPayload = {
      user: { id: userId },  // Match your backend entity structure
      title: "My Resume",    // You might want to make this dynamic
    };

    const resumeRes = await ApiService.post('/resumeDetail/create', resumeDetailPayload);
    const resumeId = resumeRes.data.id;

    // Step 3: Create Education Records
    for (const edu of educationList.value) {
      await ApiService.post('/education/create', {
        ...edu,
        resume: { id: resumeId }  // Match your entity relationship
      });
    }

    // Step 4: Create Certification Records
    for (const cert of certificationList.value) {
      await ApiService.post('/certification/create', {
        certificationTitle: cert.certificationTitle,
        issuingOrg: cert.issuingOrg,
        issueDate: cert.issueDate,
        resume: { id: resumeId }
      });
    }

    await ApiService.post('/jobseeker/create',{
      ...personalInfo.value,
      user : { id: userId }
    })

    // Step 5: Create Experience Records
    for (const exp of experienceList.value) {
      await ApiService.post('/resumeExperiences/create', {
        jobTitle: exp.jobTitle,
        companyName: exp.companyName,
        startDate: exp.startDate,
        endDate: exp.endDate,
        description: exp.description,
        resume: { id: resumeId }
      });
    }

    alert('Resume submitted successfully!');
  } catch (err) {
    console.error('Submit failed:', err);
    alert('Failed to submit resume. Please try again.');
  }
}

</script>
