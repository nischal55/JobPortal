<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Navbar at the top -->
   <navbar/>

    <!-- Page content with top padding for fixed navbar -->
    <div class="pt-[80px] px-6 py-10 max-w-4xl mx-auto">
      <div class="bg-white shadow rounded-2xl p-8">
        <!-- Job Title -->
        <h2 class="text-3xl font-bold text-gray-800 mb-6">{{ job.title }}</h2>

        <!-- Basic Job Info -->
        <div class="space-y-3 text-gray-700 mb-6">
          <div><span class="font-semibold">Job Type:</span> {{ job.type }}</div>
          <div><span class="font-semibold">Location:</span> {{ job.location }}</div>
          <div><span class="font-semibold">Salary Range:</span> {{ job.salaryRange }}</div>
          <div><span class="font-semibold">Deadline:</span> {{ formatDate(job.deadline) }}</div>
          <div><span class="font-semibold">Posted On:</span> {{ formatDateTime(job.createdAt) }}</div>
        </div>

        <!-- Job Description -->
        <div class="mb-6">
          <h3 class="text-xl font-semibold mb-2">Job Description</h3>
          <div
            class="prose max-w-none overflow-x-auto break-words"
            v-html="job.description"
          />
        </div>

        <!-- Job Requirements -->
        <div>
          <h3 class="text-xl font-semibold mb-2">Requirements</h3>
          <div
            class="prose max-w-none overflow-x-auto break-words"
            v-html="job.requirements"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import ApiService from '@/services/ApiService'
import navbar from '@/components/navbar.vue'

const route = useRoute()
const job = ref({})

onMounted(() => {
  const jobId = route.params.id
  ApiService.get(`http://localhost:8888/jobDetail/findById/${jobId}`)
    .then(res => {
      job.value = res.data
    })
    .catch(err => {
      console.error('Error fetching job detail:', err)
    })
})

// Utility format functions
const formatDate = date => new Date(date).toLocaleDateString()
const formatDateTime = date => new Date(date).toLocaleString()
</script>

<style scoped>
.prose {
  word-break: break-word;
  overflow-wrap: break-word;
}
</style>
