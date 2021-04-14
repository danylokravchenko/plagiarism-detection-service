import Vue from 'vue'
import App from './App.vue'
import router from './router'
import EventBus from './EventBus'

import { BootstrapVue, IconsPlugin } from 'bootstrap-vue'

// Import Bootstrap an BootstrapVue CSS files (order is important)
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import VueAxios from "vue-axios";
import axios from "axios";

// Make BootstrapVue available throughout your project
Vue.use(BootstrapVue)
// Optionally install the BootstrapVue icon components plugin
Vue.use(IconsPlugin)

Vue.use(VueAxios, axios)

Vue.prototype.$bus = EventBus

Vue.config.productionTip = false

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
