<template>
  <navbar />
  <div class="h-[35rem] w-full bg-black flex flex-col items-center justify-center text-white">
    <div class="text-5xl font-semibold text-center">
      Find Your Dream Job Today!
    </div>
    <div class="text-slate-300 mt-3">
      Connecting Talent with Opportunity: Your Gateway to Career Success
    </div>

    <!-- Search Input and Button -->
    <div class="flex items-center gap-2 mt-10 w-full max-w-md">
      <InputText v-model="searchQuery" type="text" placeholder="Search for jobs..." class="w-full" />
      <Button icon="pi pi-search" label="Search" @click="searchJobs" />
    </div>
  </div>
</template>

<script setup>
import navbar from '@/components/navbar.vue';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import { ref } from 'vue';

const searchQuery = ref('');

function searchJobs() {
  console.log('Searching for:', searchQuery.value);
}
</script>
