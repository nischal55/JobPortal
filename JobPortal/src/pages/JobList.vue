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
            showGridlines filterDisplay="menu" :globalFilterFields="[
              'title', 'provider.username', 'type', 'location', 'salaryRange', 'description', 'requirements'
            ]" class="text-sm">
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

            <Column header="S.N"><template #body="{ index }">{{ index + 1 }}</template></Column>
            <Column field="title" header="Job Title"><template #body="{ data }">{{ data.title }}</template></Column>
            <Column field="provider.username" header="Provider"><template #body="{ data }">{{ data.provider?.username ||
                'N/A' }}</template></Column>
            <Column field="type" header="Type"><template #body="{ data }">{{ data.type }}</template></Column>
            <Column field="location" header="Location"><template #body="{ data }">{{ data.location }}</template>
            </Column>
            <Column field="salaryRange" header="Salary"><template #body="{ data }">{{ data.salaryRange }}</template>
            </Column>
            <Column field="deadline" header="Deadline"><template #body="{ data }">{{ formatDate(data.deadline)
                }}</template>
            </Column>
            <Column field="createdAt" header="Created At"><template #body="{ data }">{{ formatDateTime(data.createdAt)
                }}</template>
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

  <!-- Dialog: Job Details + Applicants -->
  <Dialog v-model:visible="visible" modal header="Job Details" :style="{ width: '50rem' }">
    <template #header>
      <div class="inline-flex items-center gap-2">
        <Avatar image="https://primefaces.org/cdn/primevue/images/avatar/amyelsner.png" shape="circle" />
        <span class="font-bold whitespace-nowrap">{{ selectedJob?.provider?.username || 'Unknown' }}</span>
      </div>
    </template>

    <!-- Job Info -->
    <div class="flex flex-col gap-3 mb-4">
      <div class="flex items-center gap-4"><label class="font-semibold w-24">Title</label><span>{{ selectedJob?.title
          }}</span></div>
      <div class="flex items-center gap-4"><label class="font-semibold w-24">Type</label><span>{{ selectedJob?.type
          }}</span></div>
      <div class="flex items-center gap-4"><label class="font-semibold w-24">Location</label><span>{{
        selectedJob?.location }}</span></div>
      <div class="flex items-center gap-4"><label class="font-semibold w-24">Salary</label><span>{{
        selectedJob?.salaryRange }}</span></div>
      <div class="flex items-center gap-4"><label class="font-semibold w-24">Deadline</label><span>{{
        formatDate(selectedJob?.deadline) }}</span></div>
    </div>

    <!-- Applicants List -->
    <h3 class="text-lg font-semibold text-slate-600 mt-4 mb-2">Applicants</h3>
    <DataTable :value="applicants" dataKey="id" paginator :rows="5" showGridlines class="text-sm">
      <template #empty>No applicants found.</template>
      <Column header="S.N"><template #body="{ index }">{{ index + 1 }}</template></Column>
      <Column field="name" header="Name"><template #body="{ data }">{{ data.seeker.user.username }}</template></Column>
      <Column field="appliedAt" header="Applied On"><template #body="{ data }">{{ formatDateTime(data.appliedAt)
          }}</template></Column>
      <Column field="status" header="Status">
        <template #body="{ data }">
          <Tag :value="data.status" :severity="data.status === 'Pending' ? 'warn' : 'success'" />
        </template>
      </Column>
      <Column field="Action" header="Action">
        <template #body="{ data }">
          <div class="flex gap-2">
            <Button icon="pi pi-eye" class="p-button-sm" title="View Applicant Detail" @click="openDialog(data)" />
          </div>
        </template>
      </Column>
    </DataTable>

    <template #footer>
      <Button label="Rank With AI" variant="outlined" severity="secondary" />
    </template>
  </Dialog>

  <!-- Dialog: Edit Job -->
  <Dialog v-model:visible="editDialogVisible" modal header="Edit Job" :style="{ width: '50rem' }">
    <div class="space-y-4">
      <div><label class="block mb-1">Title</label>
        <InputText v-model="editingJob.title" class="w-full" />
      </div>
      <div><label class="block mb-1">Description</label>
        <Editor v-model="editingJob.description" class="w-full" editorStyle="height: 150px" />
      </div>
      <div><label class="block mb-1">Location</label>
        <InputText v-model="editingJob.location" class="w-full" />
      </div>
      <div><label class="block mb-1">Salary</label>
        <InputText v-model="editingJob.salaryRange" class="w-full" />
      </div>
      <div><label class="block mb-1">Job Type</label>
        <Dropdown v-model="editingJob.type" :options="types" class="w-full" />
      </div>
      <div><label class="block mb-1">Requirements</label>
        <Editor v-model="editingJob.requirements" class="w-full" editorStyle="height: 150px" />
      </div>
      <div><label class="block mb-1">Deadline</label><input type="date" v-model="editingJob.deadline"
          class="w-full border rounded px-3 py-2" /></div>
    </div>
    <template #footer>
      <Button label="Cancel" class="p-button-text" @click="editDialogVisible = false" />
      <Button label="Update Job" class="p-button-primary" @click="updateJob" />
    </template>
  </Dialog>

  <!-- Delete Confirmation Dialog -->
  <Dialog v-model:visible="deleteDialogVisible" modal header="Confirm Delete" :style="{ width: '30rem' }">
    <div class="text-center text-red-600 text-lg mb-4">Are you sure you want to delete this job?</div>
    <template #footer>
      <Button label="Cancel" class="p-button-text" @click="deleteDialogVisible = false" />
      <Button label="Delete" severity="danger" @click="deleteJob" />
    </template>
  </Dialog>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Sidebar from '@/components/Sidebar.vue'
import AdminNav from '@/components/AdminNav.vue'
import ApiService from '@/services/ApiService'

import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import Button from 'primevue/button'
import InputText from 'primevue/inputtext'
import InputIcon from 'primevue/inputicon'
import IconField from 'primevue/iconfield'
import Dialog from 'primevue/dialog'
import Editor from 'primevue/editor'
import Dropdown from 'primevue/dropdown'
import Avatar from 'primevue/avatar'
import Tag from 'primevue/tag'
import Toast from 'primevue/toast'
import { useToast } from 'primevue/usetoast'

const toast = useToast()
const router = useRouter()

const jobs = ref([])
const applicants = ref([])
const loading = ref(false)
const visible = ref(false)
const editDialogVisible = ref(false)
const deleteDialogVisible = ref(false)

const selectedJob = ref(null)
const editingJob = ref({})
const jobToDelete = ref(null)
const user_id = localStorage.getItem('user_id')

const types = ['full_time', 'part_time', 'internship', 'contract']

const FilterMatchMode = { CONTAINS: 'contains' }
const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  title: { value: null, matchMode: FilterMatchMode.CONTAINS },
  'provider.username': { value: null, matchMode: FilterMatchMode.CONTAINS },
  type: { value: null, matchMode: FilterMatchMode.CONTAINS },
  location: { value: null, matchMode: FilterMatchMode.CONTAINS },
  salaryRange: { value: null, matchMode: FilterMatchMode.CONTAINS },
  description: { value: null, matchMode: FilterMatchMode.CONTAINS },
  requirements: { value: null, matchMode: FilterMatchMode.CONTAINS },
})

const fetchJobs = async () => {
  loading.value = true
  try {
    const res = await ApiService.get(`/jobDetail/findByProvider/${user_id}`)
    jobs.value = res.data
  } catch (err) {
    console.error('Error fetching jobs:', err)
  } finally {
    loading.value = false
  }
}

onMounted(fetchJobs)

const clearFilter = () => {
  Object.keys(filters.value).forEach(key => (filters.value[key].value = null))
}

const formatDate = dateStr => (dateStr ? new Date(dateStr).toLocaleDateString() : '')
const formatDateTime = dateStr => (dateStr ? new Date(dateStr).toLocaleString() : '')

const openAddJob = () => {
  router.push({ name: 'jobCreateForm' })
}

const openDialog = async job => {
  selectedJob.value = JSON.parse(JSON.stringify(job))
  if (!selectedJob.value.provider) selectedJob.value.provider = { username: '', email: '' }
  visible.value = true
  try {
    const res = await ApiService.get(`/jobApplicants/findApplicantsByJobId/${job.id}`)
    applicants.value = res.data
  } catch (err) {
    console.error('Error fetching applicants:', err)
    applicants.value = []
  }
}

const openEditDialog = job => {
  editingJob.value = { ...job }
  editDialogVisible.value = true
}

const updateJob = async () => {
  try {
    await ApiService.put(`/jobDetail/updateJobDetail`, editingJob.value)
    toast.add({ severity: 'success', summary: 'Success', detail: 'Job updated', life: 3000 })
    editDialogVisible.value = false
    fetchJobs()
  } catch (err) {
    toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to update job', life: 3000 })
  }
}

const confirmDelete = job => {
  jobToDelete.value = { ...job }
  deleteDialogVisible.value = true
}

const deleteJob = async () => {
  try {
    await ApiService.delete(`/jobDetail/delete/${jobToDelete.value.id}`)
    toast.add({ severity: 'success', summary: 'Deleted', detail: 'Job deleted', life: 3000 })
    deleteDialogVisible.value = false
    fetchJobs()
  } catch (err) {
    toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to delete job', life: 3000 })
  }
}
</script>
