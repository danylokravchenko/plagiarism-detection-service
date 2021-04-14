<template>
    <div class="report" ref="component">
        <br>
        <h2>Plagiarism level</h2>
        <div class="progress" style="height: 50px;">
            <div class="progress-bar" :class="reportData.plagiarismColor" role="progressbar"
                 :style="{width: (reportData.plagiarismPercent > 10 ? reportData.plagiarismPercent : 100 )+'%'}"
                 aria-valuemin="0" aria-valuemax="100">
                {{reportData.plagiarismPercent}}%
            </div>
        </div>
        <div v-if="reportData.highlights.length > 0">
            <br>
            <h2>Hight plagiarism batches</h2>

            <div class="card" v-for="elem in reportData.highlights">
                <div class="card-body">
                    <div class="row align-items-start">
                        <div class="col-sm">
                            {{elem.textA.text}}
                        </div>
                        <div class="col-sm">
                            {{elem.textB.text}}
                        </div>
                    </div>
                    <br>
                    <div class="progress" style="height: 25px;">
                        <div class="progress-bar" :class="elem.plagiarismColor" role="progressbar"
                             :style="{width: elem.plagiarismPercent+'%'}" aria-valuemin="0" aria-valuemax="100">
                            {{elem.plagiarismPercent}}%
                        </div>
                    </div>
                </div>
            </div>

            <br>
            <h2>Full text highlights</h2>

            <div class="card">
                <div class="card-body">
                    <div class="row align-items-start">
                        <div class="col-sm" v-html="reportData.markedA"></div>
                        <div class="col-sm" v-html="reportData.markedB"></div>
                    </div>
                    <br>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Report",
        props: ['reportData'],
        methods: {
            scrollTo () {
                this.$refs.component.scrollIntoView();
            }
        }
    }
</script>

<style scoped>
    .card {
        margin-top: 20px;
    }
</style>