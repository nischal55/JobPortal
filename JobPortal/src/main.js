import './assets/main.css'
import PrimeVue from 'primevue/config';
import { createApp } from 'vue'
import App from './App.vue'
import Aura from '@primeuix/themes/aura';
import 'primeicons/primeicons.css';
import { createPinia } from 'pinia'
import router from './router'

const app = createApp(App);


app.use(createPinia())

app.use(PrimeVue, {
    theme: {
        preset: Aura
    }
});
app.use(router)
app.mount('#app');
