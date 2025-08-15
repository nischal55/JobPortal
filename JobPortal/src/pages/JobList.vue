<template>
  <div class="flex h-screen">
    <Sidebar />
    <div class="flex-1 flex flex-col w-[69%]">
      <AdminNav />
      <main class="flex-1 p-3 overflow-auto">
        <div class="card">
          <div class="flex justify-between items-center mb-4">
            <div class="text-slate-600 text-lg font-semibold p-2">
              Job List
            </div>
            <Button 
              label="Add Job" 
              icon="pi pi-plus" 
              class="p-button-sm p-button-success" 
              @click="openAddJob"
            />
          </div>

          <DataTable :value="jobs" v-model:filters="filters" dataKey="id" paginator :rows="10"
              :loading="loading" showGridlines filterDisplay="menu" :globalFilterFields="[
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
                  <template #filter="{ filterModel }">
                      <InputText v-model="filterModel.value" placeholder="Search by title" class="text-sm" />
                  </template>
              </Column>

              <Column field="provider.username" header="Provider" style="min-width: 12rem">
                  <template #body="{ data }">{{ data.provider?.username }}</template>
                  <template #filter="{ filterModel }">
                      <InputText v-model="filterModel.value" placeholder="Search provider" class="text-sm" />
                  </template>
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
          </DataTable>
        </div>
      </main>
    </div>
  </div>
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

const jobs = ref([]);
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
    for (const key in filters.value) {
        filters.value[key].value = null;
    }
};

const formatDate = (dateStr) => {
    if (!dateStr) return '';
    const date = new Date(dateStr);
    return date.toLocaleDateString();
};

const formatDateTime = (dateStr) => {
    if (!dateStr) return '';
    const date = new Date(dateStr);
    return date.toLocaleString();
};

// Open add job page
const openAddJob = () => {
    router.push({ name: 'jobCreateForm' }); // replace with your route name
};
</script>
