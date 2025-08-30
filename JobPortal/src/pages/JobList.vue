<template>
  <div class="flex h-screen">
    <Sidebar />
    <div class="flex-1 flex flex-col w-[69%]">
      <AdminNav />
      <main class="flex-1 p-3 overflow-auto">
        <div class="card rounded">
          <div class="flex justify-between items-center mb-4">
            <div class="text-slate-600 text-lg font-semibold p-2">Job List</div>
            <Button label="Add Job" icon="pi pi-plus" class="p-button-sm p-button-success" @click="openAddJob" />
          </div>

          <DataTable :value="jobs" v-model:filters="filters" dataKey="id" paginator :rows="10" :loading="loading"
            showGridlines filterDisplay="menu" :globalFilterFields="[
              'title',
              'provider.username',
              'type',
              'location',
              'salaryRange',
              'description',
              'requirements'
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

            <Column header="S.N">
              <template #body="{ index }">{{ index + 1 }}</template>
            </Column>

            <Column field="title" header="Job Title" style="min-width: 12rem">
              <template #body="{ data }">{{ data.title }}</template>
            </Column>

            <Column field="provider.username" header="Provider" style="min-width: 12rem">
              <template #body="{ data }">{{ data.provider?.username || 'N/A' }}</template>
            </Column>

            <Column field="type" header="Type" style="min-width: 10rem">
              <template #body="{ data }">{{ data.type }}</template>
            </Column>

            <Column field="location" header="Location" style="min-width: 12rem">
              <template #body="{ data }">{{ data.location }}</template>
            </Column>

            <Column field="salaryRange" header="Salary" style="min-width: 12rem">
              <template #body="{ data }">{{ data.salaryRange }}</template>
            </Column>

            <Column field="deadline" header="Deadline" style="min-width: 12rem">
              <template #body="{ data }">{{ formatDate(data.deadline) }}</template>
            </Column>

            <Column field="createdAt" header="Created At" style="min-width: 12rem">
              <template #body="{ data }">{{ formatDateTime(data.createdAt) }}</template>
            </Column>

            <Column field="Action" header="Action" class="flex gap-2">
              <template #body="{ data }">
                <Button icon="pi pi-eye" class="p-button-sm" title="View Job Applicants" @click="openDialog(data)" />

                <Button icon="pi pi-pencil" class="p-button-sm" title="update Job Details" @click="openDialog(data)" />
              </template>
            </Column>
          </DataTable>
        </div>
      </main>
    </div>
  </div>

  <!-- Job Details + Applicants Modal -->
  <Dialog v-model:visible="visible" modal header="Job Details" :style="{ width: '50rem' }">
    <template #header>
      <div class="inline-flex items-center gap-2">
        <Avatar image="https://primefaces.org/cdn/primevue/images/avatar/amyelsner.png" shape="circle" />
        <span class="font-bold whitespace-nowrap">{{ selectedJob?.provider?.username || 'Unknown' }}</span>
      </div>
    </template>

    <!-- Job Info -->
    <div class="flex flex-col gap-3 mb-4">
      <div class="flex items-center gap-4">
        <label class="font-semibold w-24">Title</label>
        <span>{{ selectedJob?.title }}</span>
      </div>
      <div class="flex items-center gap-4">
        <label class="font-semibold w-24">Type</label>
        <span>{{ selectedJob?.type }}</span>
      </div>
      <div class="flex items-center gap-4">
        <label class="font-semibold w-24">Location</label>
        <span>{{ selectedJob?.location }}</span>
      </div>
      <div class="flex items-center gap-4">
        <label class="font-semibold w-24">Salary</label>
        <span>{{ selectedJob?.salaryRange }}</span>
      </div>
      <div class="flex items-center gap-4">
        <label class="font-semibold w-24">Deadline</label>
        <span>{{ formatDate(selectedJob?.deadline) }}</span>
      </div>
    </div>
    <!-- Applicants List -->
    <h3 class="text-lg font-semibold text-slate-600 mt-4 mb-2">Applicants</h3>
    <DataTable :value="applicants" dataKey="id" paginator :rows="5" showGridlines class="text-sm">
      <template #empty>No applicants found.</template>
      <Column header="S.N">
        <template #body="{ index }">{{ index + 1 }}</template>
      </Column>
      <Column field="name" header="Name">
        <template #body="{ data }">{{ data.seeker.user.username }}</template>
      </Column>
      <Column field="appliedAt" header="Applied On">
        <template #body="{ data }">{{ formatDateTime(data.appliedAt) }}</template>
      </Column>
      <Column field="status" header="Status">
        <template #body="{ data }">
          <Tag :value="data.status" :severity="data.status === 'Pending' ? 'warn' : 'success'" />
        </template>
      </Column>

      <Column field="Action" header="Action" class="flex gap-2">
        <template #body="{ data }">
          <Button icon="pi pi-eye" class="p-button-sm" title="View Applicant Detail" @click="openDialog(data)" />
        </template>
      </Column>
    </DataTable>

    <template #footer>
      <Button label="Cancel" text severity="secondary" @click="visible = false" />
      <Button label="Save" variant="outlined" severity="secondary" @click="saveChanges" />
    </template>
  </Dialog>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import Sidebar from '@/components/Sidebar.vue';
import AdminNav from '@/components/AdminNav.vue';
import ApiService from '@/services/ApiService';
import { useRouter } from 'vue-router';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';
import IconField from 'primevue/iconfield';
import InputIcon from 'primevue/inputicon';
import Dialog from 'primevue/dialog';
import Avatar from 'primevue/avatar';
import Tag from 'primevue/tag';

const jobs = ref([]);
const applicants = ref([]);
const loading = ref(false);
const router = useRouter();

const FilterMatchMode = { CONTAINS: 'contains' };
const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  title: { value: null, matchMode: FilterMatchMode.CONTAINS },
  'provider.username': { value: null, matchMode: FilterMatchMode.CONTAINS },
  type: { value: null, matchMode: FilterMatchMode.CONTAINS },
  location: { value: null, matchMode: FilterMatchMode.CONTAINS },
  salaryRange: { value: null, matchMode: FilterMatchMode.CONTAINS },
  description: { value: null, matchMode: FilterMatchMode.CONTAINS },
  requirements: { value: null, matchMode: FilterMatchMode.CONTAINS },
});

const visible = ref(false);
const selectedJob = ref(null);

const fetchJobs = async () => {
  loading.value = true;
  try {
    const res = await ApiService.get('/jobDetail/findAll');
    jobs.value = res.data;
  } catch (err) {
    console.error('Error fetching jobs:', err);
  } finally {
    loading.value = false;
  }
};

onMounted(fetchJobs);

const clearFilter = () => {
  Object.keys(filters.value).forEach((key) => (filters.value[key].value = null));
};

const formatDate = (dateStr) => {
  if (!dateStr) return '';
  return new Date(dateStr).toLocaleDateString();
};
const formatDateTime = (dateStr) => {
  if (!dateStr) return '';
  return new Date(dateStr).toLocaleString();
};

const openAddJob = () => {
  router.push({ name: 'jobCreateForm' });
};

// Open dialog and load applicants
const openDialog = async (job) => {
  selectedJob.value = JSON.parse(JSON.stringify(job));
  if (!selectedJob.value.provider) selectedJob.value.provider = { username: '', email: '' };
  visible.value = true;

  // fetch applicants for this job
  try {
    const res = await ApiService.get(`/jobApplicants/findApplicantsByJobId/${job.id}`);
    applicants.value = res.data;
  } catch (err) {
    console.error('Error fetching applicants:', err);
    applicants.value = [];
  }
};

// Save changes locally (or call API)
const saveChanges = () => {
  if (!selectedJob.value) return;
  const index = jobs.value.findIndex((j) => j.id === selectedJob.value.id);
  if (index !== -1) jobs.value[index] = JSON.parse(JSON.stringify(selectedJob.value));
  visible.value = false;
};
</script>
