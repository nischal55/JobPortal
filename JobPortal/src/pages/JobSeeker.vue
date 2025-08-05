<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios'; // install via: npm i axios

import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';
import IconField from 'primevue/iconfield';
import InputIcon from 'primevue/inputicon';
import Sidebar from '@/components/Sidebar.vue';
import AdminNav from '@/components/AdminNav.vue';
import ApiService from '@/services/ApiService';

// State variables
const jobseekers = ref([]);
const loading = ref(false);

// Filter constants
const FilterMatchMode = {
  CONTAINS: 'contains',
  EQUALS: 'equals',
};

// Filters
const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  'user.name': { value: null, matchMode: FilterMatchMode.CONTAINS },
  contactNo: { value: null, matchMode: FilterMatchMode.CONTAINS },
  address: { value: null, matchMode: FilterMatchMode.CONTAINS },
  skills: { value: null, matchMode: FilterMatchMode.CONTAINS },
});

// Fetch job seekers from backend
const fetchJobSeekers = async () => {
  loading.value = true;
  try {
    const response = await ApiService.get('http://localhost:8888/jobseeker/findAll');
    jobseekers.value = response.data;
  } catch (err) {
    console.error('Error fetching job seekers:', err);
  } finally {
    loading.value = false;
  }
};

onMounted(fetchJobSeekers);

const clearFilter = () => {
  for (const key in filters.value) {
    filters.value[key].value = null;
  }
};
</script>

<template>
  <div class="flex h-screen">
    <Sidebar />
    <div class="flex-1 flex flex-col w-[69%]">
      <AdminNav />
      <main class="flex-1 p-3 overflow-auto">
        <div class="card">
          <DataTable
            :value="jobseekers"
            v-model:filters="filters"
            dataKey="id"
            paginator
            :rows="10"
            :loading="loading"
            showGridlines
            filterDisplay="menu"
            :globalFilterFields="['user.username', 'contactNo', 'address', 'skills']"
            class="text-sm"
          >
            <template #header>
              <div class="flex justify-between items-center">
                <Button icon="pi pi-filter-slash" label="Clear" outlined @click="clearFilter" />
                <IconField>
                  <InputIcon><i class="pi pi-search" /></InputIcon>
                  <InputText v-model="filters.global.value" placeholder="Keyword Search" />
                </IconField>
              </div>
            </template>

            <template #empty>No job seekers found.</template>
            <template #loading>Loading job seeker data. Please wait.</template>

            <Column header="S.N"> 
              <template #body="{ index }">{{ index+1 }}</template>
            </Column>

            <Column field="user.username" header="Name" style="min-width: 12rem">
              <template #body="{ data }">{{ data.user?.username }}</template>
              <template #filter="{ filterModel }">
                <InputText v-model="filterModel.value" placeholder="Search by name" class="text-sm"/>
              </template>
            </Column>

            <Column field="contactNo" header="Contact No" style="min-width: 10rem">
              <template #body="{ data }">{{ data.contactNo }}</template>
              <template #filter="{ filterModel }">
                <InputText v-model="filterModel.value" placeholder="Search contact no" class="text-sm"/>
              </template>
            </Column>

            <Column field="address" header="Address" style="min-width: 12rem">
              <template #body="{ data }">{{ data.address }}</template>
              <template #filter="{ filterModel }">
                <InputText v-model="filterModel.value" placeholder="Search by address" class="text-sm" />
              </template>
            </Column>

            <Column field="skills" header="Skills" style="min-width: 12rem">
              <template #body="{ data }">{{ data.skills }}</template>
              <template #filter="{ filterModel }">
                <InputText v-model="filterModel.value" placeholder="Search by skills" class="text-sm"/>
              </template>
            </Column>

            <Column field="summary" header="Summary" style="min-width: 14rem">
              <template #body="{ data }">{{ data.summary }}</template>
            </Column>

            <Column field="linkedIn" header="LinkedIn" style="min-width: 12rem">
              <template #body="{ data }">
                <a :href="data.linkedIn" class="text-blue-500 underline" target="_blank">{{ data.linkedIn }}</a>
              </template>
            </Column>
          </DataTable>
        </div>
      </main>
    </div>
  </div>
</template>

<style scoped>
.card {
  padding: 2rem;
  margin: 2rem;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
</style>
