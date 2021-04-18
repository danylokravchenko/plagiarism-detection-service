<template>
    <b-card no-body class="mb-1" style="border: none;">
        <b-card-header header-tag="header" class="p-1" role="tab" style="background: white; border: none;">
            <b-button block v-b-toggle="'accordion-' + elem.id" variant="outline-success">Detection #{{elem.id}} by {{elem.createdAt}}</b-button>
        </b-card-header>
        <b-collapse :id="'accordion-'+elem.id" accordion="my-accordion" role="tabpanel">
            <b-card-body>
                <h2>Plagiarism level</h2>
                <div class="progress" style="height: 50px;">
                    <div class="progress-bar" :class="percentToColor(probToPercent(elem.plagiarismLevel))" role="progressbar"
                         :style="{width: (probToPercent(elem.plagiarismLevel) > 10 ? probToPercent(elem.plagiarismLevel)  : 100 )+'%'}"
                         aria-valuemin="0" aria-valuemax="100">
                        {{probToPercent(elem.plagiarismLevel)}}%
                    </div>
                </div>


                <div class="row align-items-start" style="margin-top: 20px">
                    <div class="col-sm">
                        <div class="card">
                            <div class="card-body">
                                {{elem.textA}}
                            </div>
                        </div>
                    </div>
                    <div class="col-sm">
                        <div class="card">
                            <div class="card-body">{{elem.textB}}</div>
                        </div>
                    </div>
                </div>
                <br>
                <!--                <b-card-text>{{ elem }}</b-card-text>-->
            </b-card-body>
        </b-collapse>
    </b-card>

</template>

<script>
    export default {
        name: "HistoryElem",
        props: ['elem'],
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
            }
        }
    }
</script>

<style scoped>

</style>