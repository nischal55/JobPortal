<template>
  <div class="flex h-screen">
    <Sidebar />
    <div class="flex-1 flex flex-col w-[69%]">
      <AdminNav />
      <div class="w-11/12 mx-auto mt-10 p-8 shadow rounded bg-white">
        <h2 class="text-emerald-600 font-semibold text-xl mb-6">Job Provider Information</h2>
        
        <form @submit.prevent="handleSubmit" class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <!-- Company Name -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Company Name</label>
            <input type="text" v-model="form.companyName" required class="w-full border rounded px-3 py-2" />
          </div>

          <!-- Website -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Website</label>
            <input type="url" v-model="form.website" class="w-full border rounded px-3 py-2" />
          </div>

          <!-- Contact Number -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Contact Number</label>
            <input type="text" v-model="form.contactNo" required class="w-full border rounded px-3 py-2" />
          </div>

          <!-- Contact Email -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Contact Email</label>
            <input type="email" v-model="form.contactEmail" required class="w-full border rounded px-3 py-2" />
          </div>

          <!-- Description (full width) -->
          <div class="md:col-span-2">
            <label class="block text-sm font-medium text-gray-700 mb-1">Description</label>
            <textarea v-model="form.description" required rows="4" class="w-full border rounded px-3 py-2"></textarea>
          </div>

          <!-- Submit Button (full width) -->
          <div class="md:col-span-2 flex justify-end">
            <button type="submit" class="bg-emerald-600 text-white px-6 py-2 rounded hover:bg-emerald-700">
              Submit
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import Sidebar from '@/components/Sidebar.vue'
import AdminNav from '@/components/AdminNav.vue'
import ApiService from '@/services/ApiService'

const form = ref({
  companyName: '',
  website: '',
  contactNo: '',
  contactEmail: '',
  description: '',
  userId: localStorage.getItem('user_id')
})

const handleSubmit = async () => {
  try {
    const payload = {
      companyName: form.value.companyName,
      website: form.value.website,
      contactNo: form.value.contactNo,
      contactEmail: form.value.contactEmail,
      description: form.value.description,
      user: {
        id: form.value.userId
      }
    }

    const response = await ApiService.post('/jobproviders/create', payload)
    console.log('Success:', response.data)
    alert('Job provider detail submitted successfully!')
  } catch (error) {
    console.error('Error:', error)
    alert('Something went wrong while submitting.')
  }
}
</script>
