<template>
  <footer class="bg-gray-900 text-white py-10 px-6">
    <div class="max-w-7xl mx-auto grid grid-cols-1 md:grid-cols-3 gap-10">
      
      <!-- Company Info -->
      <div>
        <h3 class="text-xl font-bold mb-3">Job Portal</h3>
        <p class="text-sm text-gray-400">
         Find Your Dream Job Today!
        </p>
      </div>

      <!-- Useful Links -->
      <div>
        <h3 class="text-xl font-bold mb-3">Links</h3>
        <ul class="space-y-2 text-gray-300">
          <li><a href="#" class="hover:text-white transition">Home</a></li>
          <li><a href="#" class="hover:text-white transition">About</a></li>
          <li><a href="#" class="hover:text-white transition">Services</a></li>
          <li><a href="#" class="hover:text-white transition">Contact</a></li>
        </ul>
      </div>

      <!-- Social Media -->
      <div>
        <h3 class="text-xl font-bold mb-3">Follow Us</h3>
        <div class="flex gap-4">
          <Button icon="pi pi-facebook" rounded text severity="secondary" class="hover:text-blue-400" />
          <Button icon="pi pi-twitter" rounded text severity="secondary" class="hover:text-blue-400" />
          <Button icon="pi pi-linkedin" rounded text severity="secondary" class="hover:text-blue-400" />
          <Button icon="pi pi-github" rounded text severity="secondary" class="hover:text-blue-400" />
        </div>
      </div>

    </div>

    <!-- Bottom -->
    <div class="mt-10 border-t border-gray-700 pt-4 text-center text-gray-500 text-sm">
      © 2025 Nischal Shakya. All rights reserved.
    </div>
  </footer>
</template>

<script setup>
import Button from 'primevue/button'
</script>
