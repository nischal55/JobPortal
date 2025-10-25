<template>
  <div class="flex h-screen">
    <Sidebar />
    <div class="flex-1 flex flex-col w-[69%]">
      <AdminNav />
      <main class="flex-1 p-3 overflow-auto">
        <Toast />
        <div class="card rounded">
          <div class="flex justify-between items-center mb-4">
            <div class="text-slate-600 text-lg font-semibold p-2">Job List</div>
            <Button label="Add Job" icon="pi pi-plus" class="p-button-sm" @click="openAddJob" />
          </div>

          <DataTable :value="jobs" v-model:filters="filters" dataKey="id" paginator :rows="10" :loading="loading"
            showGridlines filterDisplay="menu"
            :globalFilterFields="['title', 'provider.username', 'type', 'location', 'salaryRange', 'description', 'requirements']"
            class="text-sm">
            <template #header>
              <div class="flex justify-between items-center">
                <Button icon="pi pi-filter-slash" label="Clear" outlined @click="clearFilter" />
                <IconField>
                  <InputIcon><i class="pi pi-search" /></InputIcon>
                  <InputText v-model="filters.global.value" placeholder="Keyword Search" />
                </IconField>
              </div>
            </template>

            <template #empty>No job details found.</template>
            <template #loading>Loading jobs. Please wait.</template>

            <Column header="S.N">
              <template #body="{ index }">{{ index + 1 }}</template>
            </Column>
            <Column field="title" header="Job Title">
              <template #body="{ data }">{{ data.title }}</template>
            </Column>
            <Column field="provider.username" header="Provider">
              <template #body="{ data }">{{ data.provider?.username || 'N/A' }}</template>
            </Column>
            <Column field="type" header="Type">
              <template #body="{ data }">{{ data.type }}</template>
            </Column>
            <Column field="location" header="Location">
              <template #body="{ data }">{{ data.location }}</template>
            </Column>
            <Column field="salaryRange" header="Salary">
              <template #body="{ data }">{{ data.salaryRange }}</template>
            </Column>
            <Column field="deadline" header="Deadline">
              <template #body="{ data }">{{ formatDate(data.deadline) }}</template>
            </Column>
            <Column field="createdAt" header="Created At">
              <template #body="{ data }">{{ formatDateTime(data.createdAt) }}</template>
            </Column>

            <Column field="Action" header="Action">
              <template #body="{ data }">
                <div class="flex gap-2">
                  <Button icon="pi pi-eye" class="p-button-sm" title="View Applicants" @click="openDialog(data)" />
                  <Button icon="pi pi-pencil" class="p-button-sm p-button-warning" title="Edit Job"
                    @click="openEditDialog(data)" />
                  <Button icon="pi pi-trash" class="p-button-sm p-button-danger" title="Delete Job"
                    @click="confirmDelete(data)" />
                </div>
              </template>
            </Column>
          </DataTable>
        </div>
      </main>
    </div>
  </div>

  <!-- Job Details + Applicants -->
  <Dialog v-model:visible="visible" modal header="Job Details" :style="{ width: '50rem' }">
    <template #header>
      <div class="inline-flex items-center gap-2">
        <Avatar image="https://primefaces.org/cdn/primevue/images/avatar/amyelsner.png" shape="circle" />
        <span class="font-bold whitespace-nowrap">{{ selectedJob?.provider?.username || 'Unknown' }}</span>
      </div>
    </template>

    <!-- Job Info -->
    <div class="flex flex-col gap-3 mb-4">
      <div class="flex items-center gap-4"><label class="font-semibold w-24">Title</label><span>{{ selectedJob?.title }}</span></div>
      <div class="flex items-center gap-4"><label class="font-semibold w-24">Type</label><span>{{ selectedJob?.type }}</span></div>
      <div class="flex items-center gap-4"><label class="font-semibold w-24">Location</label><span>{{ selectedJob?.location }}</span></div>
      <div class="flex items-center gap-4"><label class="font-semibold w-24">Salary</label><span>{{ selectedJob?.salaryRange }}</span></div>
      <div class="flex items-center gap-4"><label class="font-semibold w-24">Deadline</label><span>{{ formatDate(selectedJob?.deadline) }}</span></div>
    </div>

    <!-- Applicants -->
    <h3 class="text-lg font-semibold text-slate-600 mt-4 mb-2">Applicants</h3>
    <DataTable :value="applicants" dataKey="id" paginator :rows="5" showGridlines class="text-sm">
      <template #empty>No applicants found.</template>
      <Column header="S.N"><template #body="{ index }">{{ index + 1 }}</template></Column>
      <Column field="name" header="Name"><template #body="{ data }">{{ data.seeker.user.username }}</template></Column>
      <Column field="appliedAt" header="Applied On"><template #body="{ data }">{{ formatDateTime(data.appliedAt) }}</template></Column>
      <Column field="status" header="Status">
        <template #body="{ data }">
          <Tag :value="data.status" class="capitalize"
            :severity="data.status === 'applied' ? 'warn' : data.status === 'reviewing' ? 'info' : data.status === 'accepted' ? 'success' : 'danger'" />
        </template>
      </Column>
      <Column field="Action" class="w-20" header="Action">
        <template #body="{ data }">
          <div class="flex gap-2">
            <Button icon="pi pi-eye" class="p-button-sm" title="View Applicant Detail" @click="viewResume(data)" />
            <Button icon="pi pi-arrow-circle-right" class="p-button-sm" :disabled="data.status !== 'applied'"
              title="Review Applicant" @click="confirmReview(data)" />
            <Button icon="pi pi-check" class="p-button-sm" :disabled="data.status !== 'reviewing'"
              title="Approve Applicant" @click="confirmApprove(data)" />
            <Button icon="pi pi-times" class="p-button-sm p-button-danger" :disabled="data.status !== 'reviewing'"
              title="Reject Applicant" @click="confirmReject(data)" />
          </div>
        </template>
      </Column>
    </DataTable>

    <template #footer>
      <Button label="Rank With AI" variant="outlined" severity="secondary" @click="rankWithAI" />
    </template>
  </Dialog>

  <!-- Applicant Resume Dialog -->
  <Dialog v-model:visible="applicantDialogVisible" modal header="Applicant Detail" :style="{ width: '45rem' }" class="p-4">
    <div v-if="selectedApplicant" class="space-y-5">
      <div class="flex items-center gap-4 border-b border-slate-400 pb-3">
        <Avatar :image="selectedApplicant?.seeker?.user?.profileImage || 'https://primefaces.org/cdn/primevue/images/avatar/amyelsner.png'"
                shape="circle" size="xlarge" class="border border-gray-200 shadow-sm" />
        <div>
          <h2 class="font-bold text-xl">{{ selectedApplicant?.seeker?.user?.username }}</h2>
          <p class="text-gray-500">{{ selectedApplicant?.seeker?.user?.email }}</p>
          <p class="text-gray-500">Phone: {{ selectedApplicant?.seeker?.contactNo || 'N/A' }}</p>
        </div>
      </div>

      <div class="bg-gray-50 p-3 rounded shadow-sm">
        <h4 class="font-semibold text-md mb-1">Resume Summary</h4>
        <p class="text-gray-700">{{ selectedApplicant?.seeker?.summary || 'N/A' }}</p>
        <p class="text-gray-500 mt-1 text-sm">Applied On: {{ formatDateTime(selectedApplicant?.appliedAt) }}</p>
      </div>

      <div v-if="applicantEducation.length" class="bg-white p-3 rounded shadow-sm border border-slate-300">
        <h4 class="font-semibold text-md mb-2">Education</h4>
        <ul class="space-y-1">
          <li v-for="(edu, index) in applicantEducation" :key="index" class="flex justify-between">
            <span>{{ edu.degree }} - {{ edu.institution }}</span>
            <span class="text-gray-500 text-sm">({{ edu.startYear || 'N/A' }} - {{ edu.endYear || 'N/A' }})</span>
          </li>
        </ul>
      </div>

      <div v-if="applicantCertifications.length" class="bg-white p-3 rounded shadow-sm border border-slate-300">
        <h4 class="font-semibold text-md mb-2">Certifications</h4>
        <ul class="space-y-1">
          <li v-for="(cert, index) in applicantCertifications" :key="index" class="flex justify-between">
            <span>{{ cert.certificationTitle }} - {{ cert.issuingOrg }}</span>
            <span class="text-gray-500 text-sm">({{ cert.issueDate || 'N/A' }})</span>
          </li>
        </ul>
      </div>

      <div v-if="applicantExperience.length" class="bg-white p-3 rounded shadow-sm border border-slate-300">
        <h4 class="font-semibold text-md mb-2">Experience</h4>
        <ul class="space-y-1">
          <li v-for="(exp, index) in applicantExperience" :key="index" class="flex justify-between">
            <span>{{ exp.jobTitle }} at {{ exp.companyName }}</span>
            <span class="text-gray-500 text-sm">({{ exp.startDate || 'N/A' }} - {{ exp.endDate || 'N/A' }})</span>
          </li>
        </ul>
      </div>
    </div>

    <template #footer>
      <Button label="Close" class="p-button-text" @click="applicantDialogVisible = false" />
    </template>
  </Dialog>

  <!-- Ranked Applicants Dialog -->
  <Dialog v-model:visible="rankedDialogVisible" modal header="Ranked Applicants" :style="{ width: '50rem' }">
    <DataTable :value="rankedApplicants" dataKey="id" paginator :rows="5" showGridlines class="text-sm">
      <Column header="Rank"><template #body="{ index }">{{ index + 1 }}</template></Column>
      <Column field="username" header="Name"><template #body="{ data }">{{ data.username }}</template></Column>
      <Column field="summary" header="Summary"><template #body="{ data }">{{ data.summary || 'N/A' }}</template></Column>
      <Column field="aiScore" header="AI Score"><template #body="{ data }">{{ data.aiScore.toFixed(2) }}</template></Column>
    </DataTable>

    <template #footer>
      <Button label="Close" class="p-button-text" @click="rankedDialogVisible = false" />
    </template>
  </Dialog>

  <!-- Edit Job Dialog -->
  <Dialog v-model:visible="editDialogVisible" modal header="Update Job" :style="{ width: '45rem' }" class="p-4">
    <form @submit.prevent="updateJob" class="space-y-4">
      <div>
        <label class="block text-sm font-medium mb-1">Job Title</label>
        <InputText v-model="editJob.title" placeholder="e.g. Software Engineer" class="w-full" />
      </div>

      <div>
        <label class="block text-sm font-medium mb-1">Description</label>
        <Editor 
          v-model="editJob.description" 
          editorStyle="height: 150px" 
          class="w-full"
          :modules="{ toolbar: true }"
        />
      </div>

      <div>
        <label class="block text-sm font-medium mb-1">Location</label>
        <InputText v-model="editJob.location" placeholder="e.g. Kathmandu, Nepal" class="w-full" />
      </div>

      <div>
        <label class="block text-sm font-medium mb-1">Salary Range</label>
        <InputText v-model="editJob.salaryRange" placeholder="e.g. 50000 - 80000 NPR" class="w-full" />
      </div>

      <div>
        <label class="block text-sm font-medium mb-1">Job Type</label>
        <Dropdown v-model="editJob.type" :options="types" placeholder="Select job type" class="w-full" />
      </div>

      <div>
        <label class="block text-sm font-medium mb-1">Requirements</label>
        <Editor 
          v-model="editJob.requirements" 
          editorStyle="height: 120px" 
          class="w-full"
          :modules="{ toolbar: true }"
        />
      </div>

      <div>
        <label class="block text-sm font-medium mb-1">Deadline</label>
        <input type="date" v-model="editJob.deadline" class="w-full border border-gray-300 rounded px-3 py-2" />
      </div>

      <div class="flex justify-end gap-3 mt-5">
        <Button label="Cancel" severity="secondary" @click="editDialogVisible = false" />
        <Button label="Update" type="submit" />
      </div>
    </form>
  </Dialog>

  <!-- Delete Confirmation Dialog -->
  <Dialog v-model:visible="deleteDialogVisible" modal header="Confirm Delete" :style="{ width: '25rem' }">
    <div class="confirmation-content">
      <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem" />
      <span>Are you sure you want to delete this job?</span>
    </div>
    <template #footer>
      <Button label="No" icon="pi pi-times" class="p-button-text" @click="deleteDialogVisible = false" />
      <Button label="Yes" icon="pi pi-check" class="p-button-danger" @click="deleteJob" />
    </template>
  </Dialog>
</template>

<script setup>
import { ref, onMounted } from "vue"
import { useRouter } from "vue-router"
import Sidebar from "@/components/Sidebar.vue"
import AdminNav from "@/components/AdminNav.vue"
import ApiService from "@/services/ApiService"
import DataTable from "primevue/datatable"
import Column from "primevue/column"
import Button from "primevue/button"
import InputText from "primevue/inputtext"
import InputIcon from "primevue/inputicon"
import IconField from "primevue/iconfield"
import Dialog from "primevue/dialog"
import Avatar from "primevue/avatar"
import Tag from "primevue/tag"
import Toast from "primevue/toast"
import Dropdown from 'primevue/dropdown'
import Editor from 'primevue/editor'
import { useToast } from "primevue/usetoast"
import axios from "axios"

const toast = useToast()
const router = useRouter()

// ---------------- STATE ----------------
const jobs = ref([])
const applicants = ref([])
const applicantResumes = ref([])
const rankedApplicants = ref([])

const loading = ref(false)
const visible = ref(false) // Job details dialog
const applicantDialogVisible = ref(false)
const rankedDialogVisible = ref(false)
const editDialogVisible = ref(false) // Edit job dialog
const deleteDialogVisible = ref(false) // Delete confirmation dialog

const selectedJob = ref(null)
const selectedApplicant = ref(null)
const applicantEducation = ref([])
const applicantCertifications = ref([])
const applicantExperience = ref([])
const jobToDelete = ref(null)

const editJob = ref({
  id: null,
  title: "",
  description: "",
  location: "",
  salaryRange: "",
  type: "",
  requirements: "",
  deadline: ""
})

const types = ["full_time", "part_time", "internship", "contract"]
const user_id = localStorage.getItem("user_id")

// ---------------- FILTERS ----------------
// Define FilterMatchMode locally if import fails
const FilterMatchMode = {
  STARTS_WITH: 'startsWith',
  CONTAINS: 'contains',
  NOT_CONTAINS: 'notContains',
  ENDS_WITH: 'endsWith',
  EQUALS: 'equals',
  NOT_EQUALS: 'notEquals',
  IN: 'in',
  LESS_THAN: 'lt',
  LESS_THAN_OR_EQUAL_TO: 'lte',
  GREATER_THAN: 'gt',
  GREATER_THAN_OR_EQUAL_TO: 'gte',
  BETWEEN: 'between',
  DATE_IS: 'dateIs',
  DATE_IS_NOT: 'dateIsNot',
  DATE_BEFORE: 'dateBefore',
  DATE_AFTER: 'dateAfter'
}

const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  title: { value: null, matchMode: FilterMatchMode.CONTAINS },
  "provider.username": { value: null, matchMode: FilterMatchMode.CONTAINS },
  type: { value: null, matchMode: FilterMatchMode.CONTAINS },
  location: { value: null, matchMode: FilterMatchMode.CONTAINS },
  salaryRange: { value: null, matchMode: FilterMatchMode.CONTAINS },
  description: { value: null, matchMode: FilterMatchMode.CONTAINS },
  requirements: { value: null, matchMode: FilterMatchMode.CONTAINS },
})

// ---------------- FETCH JOBS ----------------
const fetchJobs = async () => {
  loading.value = true
  try {
    const res = await ApiService.get(`/jobDetail/findByProvider/${user_id}`)
    jobs.value = res.data
  } catch (err) {
    console.error("Error fetching jobs:", err)
    toast.add({ severity: "error", summary: "Error", detail: "Failed to load jobs", life: 3000 })
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchJobs()
})

// ---------------- UTILS ----------------
const clearFilter = () => {
  Object.keys(filters.value).forEach(key => (filters.value[key].value = null))
}

const formatDate = (d) => (d ? new Date(d).toLocaleDateString() : "")

const formatDateTime = (d) => (d ? new Date(d).toLocaleString() : "")

const openAddJob = () => router.push({ name: "jobCreateForm" })

// ---------------- JOB DETAILS DIALOG ----------------
const openDialog = async (job) => {
  selectedJob.value = JSON.parse(JSON.stringify(job))
  if (!selectedJob.value.provider) selectedJob.value.provider = { username: "", email: "" }
  visible.value = true

  try {
    const res = await ApiService.get(`/jobApplicants/findApplicantsByJobId/${job.id}`)
    applicants.value = res.data

    const resumeData = await Promise.all(applicants.value.map(fetchApplicantResumeData))
    applicantResumes.value = resumeData.filter(a => a !== null)
  } catch (err) {
    console.error("Error fetching applicants:", err)
    applicants.value = []
    applicantResumes.value = []
    toast.add({ severity: "error", summary: "Error", detail: "Failed to load applicants", life: 3000 })
  }
}

// ---------------- FETCH RESUME DETAILS ----------------
const fetchApplicantResumeData = async (applicant) => {
  try {
    const res = await ApiService.get(`/jobApplicants/findById/${applicant.id}`)
    const resumeId = res.data.resume?.id
    let education = [], certifications = [], experience = []

    if (resumeId) {
      const [eduRes, certRes, expRes] = await Promise.all([
        ApiService.get(`/education/findByResumeId/${resumeId}`),
        ApiService.get(`/certification/findByResumeId/${resumeId}`),
        ApiService.get(`/resumeExperiences/findByResumeId/${resumeId}`)
      ])
      education = eduRes.data || []
      certifications = certRes.data || []
      experience = expRes.data || []
    }

    return {
      id: applicant.id,
      username: res.data.seeker.user.username,
      summary: res.data.seeker.summary,
      education,
      certifications,
      experience
    }
  } catch (err) {
    console.error("Failed to fetch resume data:", applicant.id, err)
    return null
  }
}

// ---------------- VIEW APPLICANT RESUME ----------------
const viewResume = async (applicant) => {
  try {
    const res = await ApiService.get(`/jobApplicants/findById/${applicant.id}`)
    selectedApplicant.value = res.data
    const resumeId = selectedApplicant.value.resume?.id

    if (resumeId) {
      const [eduRes, certRes, expRes] = await Promise.all([
        ApiService.get(`/education/findByResumeId/${resumeId}`),
        ApiService.get(`/certification/findByResumeId/${resumeId}`),
        ApiService.get(`/resumeExperiences/findByResumeId/${resumeId}`)
      ])
      applicantEducation.value = eduRes.data || []
      applicantCertifications.value = certRes.data || []
      applicantExperience.value = expRes.data || []
    } else {
      applicantEducation.value = []
      applicantCertifications.value = []
      applicantExperience.value = []
    }

    applicantDialogVisible.value = true
  } catch {
    toast.add({ severity: "error", summary: "Error", detail: "Failed to load applicant details", life: 3000 })
  }
}

// ---------------- STATUS MANAGEMENT ----------------
const updateApplicantStatus = async (applicant, status, successMsg) => {
  try {
    await ApiService.put(`/jobApplicants/update`, { ...applicant, status })
    toast.add({ severity: "success", summary: successMsg, detail: `Application ${status}`, life: 3000 })
    if (selectedJob.value) {
      const res = await ApiService.get(`/jobApplicants/findApplicantsByJobId/${selectedJob.value.id}`)
      applicants.value = res.data
    }
  } catch {
    toast.add({ severity: "error", summary: "Error", detail: "Failed to update status", life: 3000 })
  }
}

const confirmApprove = (applicant) => updateApplicantStatus(applicant, "accepted", "Approved")
const confirmReject = (applicant) => updateApplicantStatus(applicant, "rejected", "Rejected")
const confirmReview = (applicant) => updateApplicantStatus(applicant, "reviewing", "In Review")

// ---------------- EDIT JOB ----------------
const openEditDialog = (job) => {
  console.log("Editing job:", job)
  editJob.value = { 
    ...job,
    description: job.description || "",
    requirements: job.requirements || "",
    deadline: job.deadline ? new Date(job.deadline).toISOString().split('T')[0] : ""
  }
  console.log("Edit job data:", editJob.value)
  editDialogVisible.value = true
}

const updateJob = async () => {
  try {
    await ApiService.put(`/jobDetail/updateJobDetail`, editJob.value)
    toast.add({ severity: "success", summary: "Success", detail: "Job updated successfully", life: 3000 })
    editDialogVisible.value = false
    await fetchJobs()
  } catch (err) {
    console.error("Failed to update job:", err)
    toast.add({ severity: "error", summary: "Error", detail: "Failed to update job", life: 3000 })
  }
}

// ---------------- DELETE JOB ----------------
const confirmDelete = (job) => {
  jobToDelete.value = job
  deleteDialogVisible.value = true
}

const deleteJob = async () => {
  if (!jobToDelete.value) return

  try {
    await ApiService.delete(`/jobDetail/delete/${jobToDelete.value.id}`)
    toast.add({ severity: "success", summary: "Success", detail: "Job deleted successfully", life: 3000 })
    deleteDialogVisible.value = false
    jobToDelete.value = null
    await fetchJobs()
  } catch (err) {
    console.error("Failed to delete job:", err)
    toast.add({ severity: "error", summary: "Error", detail: "Failed to delete job", life: 3000 })
  }
}

// ---------------- AI RANKING ----------------
const rankWithAI = async () => {
  if (!selectedJob.value || !applicantResumes.value.length) {
    return toast.add({ severity: "warn", summary: "No data", detail: "No applicants to rank", life: 3000 })
  }

  try {
    const payload = {
      job: {
        title: selectedJob.value.title,
        description: selectedJob.value.description,
        requirements: selectedJob.value.requirements || ""
      },
      applicants: applicantResumes.value.map(a => ({
        id: a.id,
        title: a.username,
        summary: a.summary,
        skills: "",
        education: a.education.map(e => ({ degree: e.degree, institution: e.institution })),
        experience: a.experience.map(e => ({ jobTitle: e.jobTitle, companyName: e.companyName, description: e.description })),
        certifications: a.certifications.map(c => ({ title: c.certificationTitle, org: c.issuingOrg }))
      }))
    }

    toast.add({ severity: "info", summary: "Ranking...", detail: "Sending data to AI API", life: 3000 })

    const res = await axios.post("http://localhost:5000/rank_applicants", payload)

    rankedApplicants.value = res.data.map((r) => {
      const app = applicantResumes.value.find(a => a.id === r.applicantId)
      return {
        id: r.applicantId,
        username: app.username,
        summary: app.summary,
        aiScore: r.score
      }
    })

    rankedDialogVisible.value = true
    toast.add({ severity: "success", summary: "AI Ranking Complete", detail: "Applicants ranked successfully", life: 3000 })
  } catch (err) {
    console.error("AI ranking failed:", err)
    toast.add({ severity: "error", summary: "Error", detail: "Failed to rank applicants", life: 3000 })
  }
}
</script>

<style scoped>
.card {
  background: white;
  padding: 1rem;
  border-radius: 0.5rem;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06);
}

.confirmation-content {
  display: flex;
  align-items: center;
  gap: 1rem;
}
</style>