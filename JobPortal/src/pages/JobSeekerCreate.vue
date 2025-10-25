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
        <!-- STEP 1: Personal Details -->
        <StepPanel v-slot="{ activateCallback }" value="1">
          <div class="flex flex-col gap-6">
            <div class="p-10 rounded-md shadow-sm bg-surface-50">
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label class="text-sm font-medium">Contact No <span class="text-red-500">*</span></label>
                  <InputText v-model="personalInfo.contactNo" class="w-full p-inputtext-sm h-11"
                    :class="{ 'p-invalid': errors.contactNo }" placeholder="+977-9800000000" />
                  <small v-if="errors.contactNo" class="text-red-500 text-xs">{{ errors.contactNo }}</small>
                </div>

                <div>
                  <label class="text-sm font-medium">LinkedIn</label>
                  <InputText v-model="personalInfo.linkedIn" class="w-full p-inputtext-sm h-11"
                    :class="{ 'p-invalid': errors.linkedIn }" placeholder="https://linkedin.com/in/..." />
                  <small v-if="errors.linkedIn" class="text-red-500 text-xs">{{ errors.linkedIn }}</small>
                </div>

                <div class="md:col-span-2">
                  <label class="text-sm font-medium">Address <span class="text-red-500">*</span></label>
                  <InputText v-model="personalInfo.address" class="w-full p-inputtext-sm h-11"
                    :class="{ 'p-invalid': errors.address }" placeholder="Kumaripati, Lalitpur" />
                  <small v-if="errors.address" class="text-red-500 text-xs">{{ errors.address }}</small>
                </div>

                <div class="md:col-span-2">
                  <label class="text-sm font-medium">Professional Summary <span class="text-red-500">*</span></label>
                  <InputText v-model="personalInfo.summary" class="w-full p-inputtext-sm h-11"
                    :class="{ 'p-invalid': errors.summary }" placeholder="Brief overview of your background..." />
                  <small v-if="errors.summary" class="text-red-500 text-xs">{{ errors.summary }}</small>
                </div>

                <div class="md:col-span-2">
                  <label class="text-sm font-medium">Skills <span class="text-red-500">*</span></label>
                  <InputText v-model="personalInfo.skills" class="w-full p-inputtext-sm h-11"
                    :class="{ 'p-invalid': errors.skills }" placeholder="e.g., Java, Spring Boot, Vue 3" />
                  <small v-if="errors.skills" class="text-red-500 text-xs">{{ errors.skills }}</small>
                </div>
              </div>
            </div>

            <div class="flex pt-6 justify-end">
              <Button label="Next" icon="pi pi-arrow-right" @click="() => validateStep(1, activateCallback)" />
            </div>
          </div>
        </StepPanel>

        <!-- STEP 2: Education -->
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
                  <InputText v-model="entry.degree" class="w-full p-inputtext-sm h-11"
                    :class="{ 'p-invalid': errors[`degree_${index}`] }" placeholder="e.g., BSc in CS" />
                  <small v-if="errors[`degree_${index}`]" class="text-red-500 text-xs">{{ errors[`degree_${index}`]
                    }}</small>
                </div>

                <div>
                  <label class="text-sm font-medium">Institution <span class="text-red-500">*</span></label>
                  <InputText v-model="entry.institution" class="w-full p-inputtext-sm h-11"
                    :class="{ 'p-invalid': errors[`institution_${index}`] }" placeholder="e.g., Tribhuvan University" />
                  <small v-if="errors[`institution_${index}`]" class="text-red-500 text-xs">{{
                    errors[`institution_${index}`] }}</small>
                </div>

                <div>
                  <label class="text-sm font-medium">Start Year</label>
                  <InputText v-model="entry.startYear" type="number" min="1900" max="2100"
                    class="w-full p-inputtext-sm h-11" placeholder="YYYY" />
                </div>

                <div>
                  <label class="text-sm font-medium">End Year</label>
                  <InputText v-model="entry.endYear" type="number" min="1900" max="2100"
                    class="w-full p-inputtext-sm h-11" placeholder="YYYY" />
                </div>

              </div>
            </div>

            <div class="flex pt-6 justify-between">
              <Button label="Back" severity="secondary" icon="pi pi-arrow-left" @click="activateCallback('1')" />
              <Button label="Next" icon="pi pi-arrow-right" @click="() => validateStep(2, activateCallback)" />
            </div>
          </div>
        </StepPanel>

        <!-- STEP 3: Certification -->
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
                  <InputText v-model="cert.certificationTitle" class="w-full p-inputtext-sm h-11"
                    :class="{ 'p-invalid': errors[`certificationTitle_${index}`] }" />
                  <small v-if="errors[`certificationTitle_${index}`]" class="text-red-500 text-xs">{{
                    errors[`certificationTitle_${index}`] }}</small>
                </div>

                <div>
                  <label class="text-sm font-medium">Issuing Organization <span class="text-red-500">*</span></label>
                  <InputText v-model="cert.issuingOrg" class="w-full p-inputtext-sm h-11"
                    :class="{ 'p-invalid': errors[`issuingOrg_${index}`] }" />
                  <small v-if="errors[`issuingOrg_${index}`]" class="text-red-500 text-xs">{{
                    errors[`issuingOrg_${index}`] }}</small>
                </div>

                <div class="md:col-span-2">
                  <label class="text-sm font-medium">Issue Date</label>
                  <InputText v-model="cert.issueDate" type="date" class="w-full p-inputtext-sm h-11"
                    placeholder="yyyy-mm-dd" />
                </div>
              </div>
            </div>

            <div class="flex pt-6 justify-between">
              <Button label="Back" severity="secondary" icon="pi pi-arrow-left" @click="activateCallback('2')" />
              <Button label="Next" icon="pi pi-arrow-right" @click="() => validateStep(3, activateCallback)" />
            </div>
          </div>
        </StepPanel>

        <!-- STEP 4: Experience -->
        <StepPanel v-slot="{ activateCallback }" value="4">
          <div class="flex flex-col gap-6">
            <div v-for="(exp, index) in experienceList" :key="index" class="p-10 rounded-md shadow-sm bg-surface-50">
              <div class="flex justify-between items-center mb-2">
                <h3 class="font-semibold text-base">Experience {{ index + 1 }}</h3>
                <div>
                  <Button icon="pi pi-trash" severity="danger" rounded text size="small"
                    v-if="experienceList.length > 1" @click="removeExperience(index)" />
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
                  <InputText v-model="exp.startDate" type="date" class="w-full p-inputtext-sm h-11"
                    placeholder="yyyy-mm-dd" />
                </div>

                <div>
                  <label class="text-sm font-medium">End Date</label>
                  <InputText v-model="exp.endDate" type="date" class="w-full p-inputtext-sm h-11"
                    placeholder="yyyy-mm-dd" />
                </div>

                <div class="md:col-span-2">
                  <label class="text-sm font-medium">Description</label>
                  <InputText v-model="exp.description" class="w-full p-inputtext-sm h-11"
                    placeholder="Describe your responsibilities or achievements" />
                </div>
              </div>
            </div>

            <div class="flex pt-6 justify-between">
              <Button label="Back" severity="secondary" icon="pi pi-arrow-left" @click="activateCallback('3')" />
              <Button label="Submit" icon="pi pi-check" iconPos="right" @click="validateSubmit" />
            </div>
          </div>
        </StepPanel>
      </StepPanels>
    </Stepper>
  </div>
</template>

<script setup>
import { ref } from "vue";
import Navbar from "@/components/navbar.vue";
import ApiService from "@/services/ApiService";
import {
  Button,
  Step,
  StepList,
  StepPanel,
  StepPanels,
  Stepper,
  InputText
} from "primevue";

const activeStep = ref("1");
const errors = ref({});

// Personal Info
const personalInfo = ref({
  contactNo: "",
  linkedIn: "",
  address: "",
  summary: "",
  skills: "",
});

// Lists
const educationList = ref([{ degree: "", institution: "", startYear: "", endYear: "" }]);
const certificationList = ref([{ certificationTitle: "", issuingOrg: "", issueDate: "" }]);
const experienceList = ref([{ jobTitle: "", companyName: "", startDate: "", endDate: "", description: "" }]);

// Add / Remove
const addEducation = () => educationList.value.push({ degree: "", institution: "", startYear: "", endYear: "" });
const removeEducation = (i) => educationList.value.splice(i, 1);
const addCertification = () => certificationList.value.push({ certificationTitle: "", issuingOrg: "", issueDate: "" });
const removeCertification = (i) => certificationList.value.splice(i, 1);
const addExperience = () => experienceList.value.push({ jobTitle: "", companyName: "", startDate: "", endDate: "", description: "" });
const removeExperience = (i) => experienceList.value.splice(i, 1);

// Validation
function validateStep(step, next) {
  errors.value = {};
  let valid = true;

  if (step === 1) {
    if (!personalInfo.value.contactNo.match(/^\+?\d{9,15}$/)) {
      errors.value.contactNo = "Enter a valid contact number.";
      valid = false;
    }
    if (personalInfo.value.linkedIn && !/^https:\/\/(www\.)?linkedin\.com\/.+/.test(personalInfo.value.linkedIn)) {
      errors.value.linkedIn = "Enter a valid LinkedIn URL.";
      valid = false;
    }
    if (!personalInfo.value.address) errors.value.address = "Address is required.", valid = false;
    if (!personalInfo.value.summary) errors.value.summary = "Summary is required.", valid = false;
    if (!personalInfo.value.skills) errors.value.skills = "Skills are required.", valid = false;
  }

  if (step === 2) {
    educationList.value.forEach((edu, i) => {
      if (!edu.degree) errors.value[`degree_${i}`] = "Degree is required.", valid = false;
      if (!edu.institution) errors.value[`institution_${i}`] = "Institution is required.", valid = false;
    });
  }

  if (step === 3) {
    certificationList.value.forEach((cert, i) => {
      if (!cert.certificationTitle) errors.value[`certificationTitle_${i}`] = "Title is required.", valid = false;
      if (!cert.issuingOrg) errors.value[`issuingOrg_${i}`] = "Organization is required.", valid = false;
    });
  }

  if (valid) next(String(step + 1));
}

// Submit
async function validateSubmit() {
  errors.value = {};
  let valid = true;

  experienceList.value.forEach((exp, i) => {
    if (!exp.jobTitle) errors.value[`jobTitle_${i}`] = "Job Title required.", valid = false;
    if (!exp.companyName) errors.value[`companyName_${i}`] = "Company required.", valid = false;
  });

  if (!valid) return;
  await submitResume();
}

// Submit API
async function submitResume() {
  try {
    const userId = localStorage.getItem("user_id");
    if (!userId) return alert("User ID not found. Please login again.");

    const resumeRes = await ApiService.post("/resumeDetail/create", {
      user: { id: userId },
      title: "My Resume",
    });
    const resumeId = resumeRes.data.id;

    for (const edu of educationList.value)
      await ApiService.post("/education/create", { ...edu, resume: { id: resumeId } });

    for (const cert of certificationList.value)
      await ApiService.post("/certification/create", { ...cert, resume: { id: resumeId } });

    await ApiService.post("/jobseeker/create", { ...personalInfo.value, user: { id: userId } });

    for (const exp of experienceList.value)
      await ApiService.post("/resumeExperiences/create", { ...exp, resume: { id: resumeId } });

    alert("Resume submitted successfully!");
    router.push({ name: 'userLogin' });
  } catch (err) {
    console.error("Submit failed:", err);
    alert("Failed to submit resume. Please try again.");
  }
}
</script>
