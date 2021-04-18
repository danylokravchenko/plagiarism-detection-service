<template>
    <div class="container">
        <h2>Profile</h2>
        <div class="card" style="padding: 10px">
            <h4>
                id: {{user.id}}
            </h4>
            <h4>
                username: {{user.login}}
            </h4>
        </div>


        <h3>History</h3>


        <div v-if="data">
            <div v-if="data.length > 0" class="accordion" role="tablist">
                <HistoryElem v-for="elem in data" v-bind:elem="elem"></HistoryElem>
            </div>
            <div v-else>
                <div class="alert alert-info" role="alert" v-if="error">
                    There is no history...
                </div>
            </div>
        </div>


    </div>
</template>

<script>
    import {authService} from "../services/authService";
    import HistoryElem from "../components/HistoryElem";

    export default {
        components: {HistoryElem},
        data() {
            return {
                user: null,
                data: null,
                text: "eoifnweiof"
            }
        },
        beforeCreate() {
            let local_user = authService.getUser()
            let config = {
                headers: {
                    Authorization: 'Bearer ' + local_user.token
                }
            }

            this.axios.get('http://localhost:8080/user/' + String(local_user.id), config)
                .then(response => this.user = response.data)

            this.axios.get('http://localhost:8080/user/' + String(local_user.id) + '/history', config)
                .then(response => this.user = response.data)
        }
    }
</script>

