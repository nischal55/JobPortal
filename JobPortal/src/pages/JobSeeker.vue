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
const banUser = async (userId) => {
  if (!userId) return;

  try {
    // Step 1: Fetch user info
    await ApiService.get(`/users/findById/${userId}`);

    // Step 2: Ban user
    const res = await ApiService.put(`/users/ban/${userId}`);
    alert(res.data.message || "User banned for 1 hour");

  } catch (err) {
    console.error("Error banning user:", err);
    alert("Failed to ban user.");
  }
};

</script>

<template>
  <div class="flex h-screen">
    <Sidebar />
    <div class="flex-1 flex flex-col w-[69%]">
      <AdminNav />
      <main class="flex-1 p-3 overflow-auto">
        <div class="text-left text-slate-600 text-lg font-semibold p-5">
          Job Applications
        </div>
        <DataTable :value="jobseekers" v-model:filters="filters" dataKey="id" paginator :rows="10" :loading="loading"
          showGridlines filterDisplay="menu" :globalFilterFields="['user.username', 'contactNo', 'address', 'skills']"
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

          <template #empty>No job seekers found.</template>
          <template #loading>Loading job seeker data. Please wait.</template>

          <Column header="S.N">
            <template #body="{ index }">{{ index + 1 }}</template>
          </Column>

          <Column field="user.username" header="Name" style="min-width: 12rem">
            <template #body="{ data }">{{ data.user?.username }}</template>
          </Column>

          <Column field="contactNo" header="Contact No" style="min-width: 10rem">
            <template #body="{ data }">{{ data.contactNo }}</template>
          </Column>

          <Column field="address" header="Address" style="min-width: 12rem">
            <template #body="{ data }">{{ data.address }}</template>
          </Column>

          <Column field="summary" header="Summary" style="min-width: 14rem">
            <template #body="{ data }">{{ data.summary }}</template>
          </Column>

          <Column field="Action" header="Action">
            <template #body="{ data }">
              <div class="flex gap-2">
                <Button icon="pi pi-ban" class="p-button-sm p-button-danger" title="Block Provider"  @click="banUser(data.user?.id)" />
              </div>
            </template>
          </Column>
        </DataTable>
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
