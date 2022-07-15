import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import './plugins/element.js'

createApp(App).use(router).mount('#app')
