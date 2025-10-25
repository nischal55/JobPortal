<template>
  <nav class="flex items-center justify-between px-8 py-2 bg-white shadow h-16">
    <!-- Left side: Logo only -->
    <div class="text-xl font-bold text-gray-800">
      JobPortal
    </div>

    <!-- Right side: Menu + Profile/Login -->
    <div class="flex items-center space-x-8">
      <!-- Desktop menu -->
      <div class="hidden md:flex items-center space-x-8">
        <router-link to="/" class="text-gray-700 hover:text-gray-900 flex items-center">
          <i class="pi pi-home mr-1"></i> Home
        </router-link>

        <router-link to="/allJobs" class="text-gray-700 hover:text-gray-900 flex items-center">
          <i class="pi pi-briefcase mr-1"></i> Jobs
        </router-link>

        <!-- ✅ Support Menu with green dot -->
        <router-link to="/support" class="text-gray-700 hover:text-gray-900 flex items-center relative">
          <i class="pi pi-comments mr-1"   v-if="isLoggedIn"></i> Support
          <!-- Green dot -->
          <span class="ml-1 h-2.5 w-2.5 bg-green-500 rounded-full animate-pulse"   v-if="isLoggedIn"></span>
        </router-link>
      </div>

      <!-- Authenticated User -->
      <div v-if="isLoggedIn" class="relative" @click="toggleDropdown">
        <img
          src="https://randomuser.me/api/portraits/men/32.jpg"
          alt="Profile"
          class="w-10 h-10 rounded-full cursor-pointer"
        />
        <div
          v-if="isDropdownOpen"
          class="absolute right-0 z-50 mt-2 w-48 bg-white border border-gray-200 rounded shadow-md"
        >
          <button
            @click="updateResume"
            class="block w-full px-4 py-2 text-left text-slate-600 hover:bg-gray-100 cursor-pointer"
          >
            My Profile
          </button>
          <button
            @click="myApplication"
            class="block w-full px-4 py-2 text-left text-slate-600 hover:bg-gray-100 cursor-pointer"
          >
            My Applications
          </button>
          <button
            @click="logout"
            class="block w-full px-4 py-2 text-left text-red-600 hover:bg-gray-100 cursor-pointer"
          >
            Logout
          </button>
        </div>
      </div>

      <!-- Guest User -->
      <div v-else class="flex items-center gap-4">
        <router-link
          to="/userLogin"
          class="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-100 transition"
        >
          Login
        </router-link>

        <router-link
          to="/registration"
          class="px-4 py-2 bg-emerald-600 text-white rounded-md font-medium hover:bg-blue-700 transition"
        >
          Sign Up
        </router-link>
      </div>
    </div>
  </nav>

  <!-- Mobile menu -->
  <div v-if="isMobileMenuOpen" class="md:hidden px-8 py-2 bg-white shadow">
    <router-link to="/" class="block py-2 text-gray-700 hover:bg-gray-100 flex items-center">
      Home
    </router-link>
    <router-link to="/allJobs" class="block py-2 text-gray-700 hover:bg-gray-100 flex items-center">
      Jobs
    </router-link>
    <!-- ✅ Support menu in mobile with green dot -->
    <router-link to="/support" class="block py-2 text-gray-700 hover:bg-gray-100 flex items-center">
      Support
      <span class="ml-2 h-2.5 w-2.5 bg-green-500 rounded-full animate-pulse"></span>
    </router-link>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();
const isDropdownOpen = ref(false);
const isMobileMenuOpen = ref(false);
const isLoggedIn = ref(false);

const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value;
};

const toggleMobileMenu = () => {
  isMobileMenuOpen.value = !isMobileMenuOpen.value;
};

const logout = () => {
  localStorage.removeItem("user_role");
  localStorage.removeItem("username");
  localStorage.removeItem("user_id");
  localStorage.removeItem("jobseeker_id");
  localStorage.removeItem("resume_detail_id");
  isLoggedIn.value = false;
  router.push("/userLogin");
};

const myApplication = () => {
  router.push("/myApplications");
};

const updateResume = () => {
  router.push("/updateResume");
};

// ✅ Check login status on load
onMounted(() => {
  isLoggedIn.value = !!localStorage.getItem("user_id");
});
</script>
