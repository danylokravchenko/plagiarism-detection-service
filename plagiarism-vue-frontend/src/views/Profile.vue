<template>
    <div class="container">
        <h2>Profile</h2>
        <h3>History</h3>
        {{user}}

    </div>
</template>

<script>
    import {authService} from "../services/authService";

    export default {
        data() {
            return {
                user: null,
                history: null
            }
        },
        beforeCreate() {
            let local_user = authService.getUser()
            console.log({headers: {
                Authorization: 'Bearer ' + local_user.token
            }})
            this.axios.get('http://localhost:8080/user/' + String(local_user.id) + '/history?page=0&limit=2',
                {
                    headers: {
                        Authorization: 'Bearer ' + local_user.token
                    }
                }
            ).then(response => this.user = response)
        }
    }
</script>

