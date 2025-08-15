<template>
  <!-- Fixed navbar -->
  <navbar class="fixed top-0 left-0 w-full z-[9999]" />

  <!-- Hero section with background image and translucent black overlay -->
  <div class="relative pt-[80px] h-[35rem] w-full overflow-hidden">
    <!-- Background Image -->
    <div
      class="absolute inset-0 bg-cover bg-center"
      style="background-image: url('https://images.unsplash.com/photo-1504384308090-c894fdcc538d?auto=format&fit=crop&w=1470&q=80')"
    ></div>

    <!-- Translucent Black Overlay -->
    <div class="absolute inset-0 bg-black opacity-50"></div>

    <!-- Overlay content -->
    <div class="relative h-full w-full flex flex-col items-center justify-center text-white px-4">
      <Transition name="p-slideup">
        <div v-if="ready" class="text-5xl font-semibold text-center">
          Find Your Dream Job Today!
        </div>
      </Transition>
      <Transition name="p-slideup">
        <div v-if="ready" class="text-slate-300 mt-3 text-center max-w-xl">
          Connecting Talent with Opportunity: Your Gateway to Career Success
        </div>
      </Transition>

      <!-- Search Input and Button -->
      <Transition name="p-slideup">
        <div v-if="ready" class="flex items-center gap-2 mt-10 w-full max-w-md">
          <InputText
            v-model="searchQuery"
            type="text"
            placeholder="Search for jobs..."
            class="w-full"
          />
          <Button icon="pi pi-search" @click="goToJobs" label="Search" />
        </div>
      </Transition>
    </div>
  </div>

  <!-- Job List Panel -->
  <div class="bg-gray-50 py-16 px-6 md:px-20">
    <h2 class="text-3xl font-bold text-center mb-10">Recommended Job Openings</h2>

    <TransitionGroup name="p-slideup" tag="div" class="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
      <div
        v-for="job in jobs"
        :key="job.id"
        class="bg-white p-6 rounded-xl shadow hover:shadow-lg transition"
      >
        <h3 class="text-xl font-semibold text-gray-800 mb-2">{{ job.title }}</h3>
        <div class="text-sm text-gray-500 mb-1">
          📍 {{ job.location }} &nbsp; | &nbsp; 💼 {{ formatType(job.type) }}
        </div>
        <div class="text-sm text-gray-500 mb-3">
          🕒 Deadline: {{ job.deadline }}
        </div>
        <router-link :to="`/jobDetail/${job.id}`" class="block">
          <Button label="View Details" class="w-full mt-2" />
        </router-link>
      </div>
    </TransitionGroup>
  </div>

  <!-- Footer -->
  <Footer />
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { TransitionGroup, Transition } from 'vue'
import { useRouter } from 'vue-router'

import navbar from '@/components/navbar.vue'
import Footer from '@/components/Footer.vue'
import InputText from 'primevue/inputtext'
import Button from 'primevue/button'

const searchQuery = ref('')
const jobs = ref([])
const ready = ref(false)

const router = useRouter();

function formatType(type) {
  switch (type) {
    case 'full_time': return 'Full Time'
    case 'part_time': return 'Part Time'
    case 'internship': return 'Internship'
    case 'contract': return 'Contract'
    default: return type
  }
}

const resumeId = localStorage.getItem('resume_detail_id')

async function fetchJobs() {
  try {
    const response = await axios.get(`http://localhost:8888/jobDetail/recommend/${resumeId}`)
    jobs.value = response.data
  } catch (error) {
    console.error('Failed to fetch jobs:', error)
  }
}

function goToJobs() {
  // Pass search query as URL parameter
  router.push({ name: 'allJobs', query: { q: searchQuery.value } })
}

onMounted(() => {
  fetchJobs()
  setTimeout(() => {
    ready.value = true
  }, 200)
})
</script>

<style scoped>
.p-slideup-enter-from {
  opacity: 0;
  transform: translateY(20px);
}
.p-slideup-enter-active {
  transition: all 0.8s ease;
}
.p-slideup-enter-to {
  opacity: 1;
  transform: translateY(0);
}
</style>
