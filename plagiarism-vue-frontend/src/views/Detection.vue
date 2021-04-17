<template>
    <div class="container">
        <h1>Plagiarism detection</h1>
        <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam sagittis, orci id tristique efficitur, eros
            risus semper sapien, quis semper erat nunc tincidunt justo. Nunc sit amet neque quis urna dignissim
            accumsan. Phasellus ut laoreet ipsum. Nulla suscipit ante ut rutrum fermentum. Pellentesque sed blandit
            risus. Quisque in tempor tellus. Curabitur feugiat pretium est non feugiat. Duis sit amet sagittis nunc.
            Nullam in quam eu neque accumsan sodales. In interdum lorem id risus eleifend vulputate. Maecenas
            ullamcorper congue porta. Mauris enim mi, vehicula ac nulla sit amet, commodo rhoncus orci. Curabitur tortor
            nunc, bibendum at rutrum at, ornare sed ligula. Donec porta ipsum massa, ac ultricies dolor venenatis at.
            Praesent at libero velit.</p>
        <div class="row align-items-start">
            <div class="col-sm">
                <div class="form-floating">
                    <textarea v-model="textA" class="textarea form-control"
                              placeholder="Enter first text here"></textarea>
                </div>
            </div>
            <div class="col-sm">
                <div class="form-floating">
                    <textarea v-model="textB" class="textarea form-control"
                              placeholder="Enter second text here"></textarea>
                </div>
            </div>
        </div>
        <button class="btn btn-success detect-btn" type="button" v-if="!isLoading" v-on:click="generateReport()">
            Generate report
        </button>
        <br>
        <Loading v-if="isLoading"></Loading>
        <Report v-if="reportData" v-bind:report-data="reportData" ref="report">
        </Report>
    </div>
</template>


<script>
    import Loading from "../components/Loading";
    import Report from "../components/Report";
    import {authService} from "../services/authService";

    export default {
        components: {Report, Loading},
        data() {
            return {
                isLoading: false,
                info: '',
                textA: '',
                textB: '',
                reportData: null
            }
        },
        methods: {
            percentToColor(percent) {
                if (percent < 10) {
                    return 'bg-success';
                } else if (percent < 50) {
                    return 'bg-warning';
                } else {
                    return 'bg-danger';
                }
            },
            probToPercent(prob) {
                return Math.round(prob * 100)
            },

            handleResponse(response){
                this.responseData = response.data
                this.responseData.plagiarismPercent = this.probToPercent(this.responseData.plagiarismLevel)
                this.responseData.plagiarismColor = this.percentToColor(this.responseData.plagiarismPercent)
                this.responseData.markedA = this.textA
                this.responseData.markedB = this.textB

                this.responseData.highlights.forEach(elem => {
                    elem.plagiarismPercent = this.probToPercent(elem.plagiarismLevel)
                    elem.plagiarismColor = this.percentToColor(elem.plagiarismPercent)
                    elem.textA.text = this.textA.slice(elem.textA.startIndex, elem.textA.endIndex)
                    elem.textB.text = this.textB.slice(elem.textB.startIndex, elem.textB.endIndex)

                    this.responseData.markedA = this.responseData.markedA.slice(0, elem.textA.startIndex)
                        + '<mark>' + this.textA.slice(elem.textA.startIndex, elem.textA.endIndex)
                        + '</mark>' + this.textA.slice(elem.textA.endIndex, this.textA.lenght)
                    this.responseData.markedB = this.responseData.markedB.slice(0, elem.textB.startIndex)
                        + '<mark>' + this.textB.slice(elem.textB.startIndex, elem.textB.endIndex)
                        + '</mark>' + this.textB.slice(elem.textB.endIndex, this.textB.lenght)

                });

                this.reportData = this.responseData;
                this.isLoading = false;

                this.$refs.report.scrollTo();
            },

            generateReport() {
                this.isLoading = true;

                let user = authService.getUser();

                if (user){

                    this.axios
                        .post('http://localhost:8080/plagiarism', {
                            textA: this.textA,
                            textB: this.textB
                        }, {
                            headers: {
                                Authorization: 'Bearer ' + user.token
                            }
                        })
                        .then(response => this.handleResponse(response))
                        .catch(error => this.info=error);
                }
                else {
                    this.axios
                        .post('http://localhost:8080/plagiarism', {
                            textA: this.textA,
                            textB: this.textB
                        })
                        .then(response => this.handleResponse(response))
                        .catch(error => this.info=error);
                }


            }
        }
    }
</script>

<style scoped>
    .textarea {
        height: 350px !important;
        margin-top: 10px;
    }

    .detect-btn {
        margin-top: 20px;
        width: 100%;
    }
</style>