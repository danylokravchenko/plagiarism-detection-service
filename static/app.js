$(function() {
    function to_percents(prob) {
        return Math.round(prob * 100);
    }

    function percent_to_color_class(percent) {
        if (percent < 10) {
            return 'bg-success';
        } else if (percent < 50) {
            return 'bg-warning';
        } else {
            return 'bg-danger';
        }
    }

    function generate_main_progres_bar(data) {
        let plagiarismPercent = to_percents(data.plagiarismLevel);

        let html = `
				<h2>Plagiarism level</h2>
				<div class="progress" style="height: 50px;">
					<div class="progress-bar ${percent_to_color_class(plagiarismPercent)}" role="progressbar"
						style="width: ${plagiarismPercent > 10? plagiarismPercent: 100}%;" aria-valuenow="${plagiarismPercent}" 
						aria-valuemin="0" aria-valuemax="100">
					${plagiarismPercent}%
					</div>
				</div>`;
        $('#report').append(html);
    }


    function generate_hight_batches(text_A, text_B, data) {
        $('#report').append(`<br><h2>Hight plagiarism batches</h2>`);
        data.highlights.forEach(function(elem) {
            let plagiarismPercent = to_percents(elem.plagiarismLevel);

            let html = `
				<div class="card" style="margin-top: 20px;">
					<div class="card-body">
						<div class="row align-items-start">
							<div class="col-sm">
								${text_A.slice(elem.textA.startIndex, elem.textA.endIndex)}
							</div>
							<div class="col-sm">
								${text_B.slice(elem.textB.startIndex, elem.textB.endIndex)}
							</div>
						</div>
						<br>
						<div class="progress" style="height: 25px;">
							<div class="progress-bar ${percent_to_color_class(plagiarismPercent)}" role="progressbar"
								style="width: ${plagiarismPercent}%;" aria-valuenow="${plagiarismPercent}" 
								aria-valuemin="0" aria-valuemax="100">
							${plagiarismPercent}%
							</div>
						</div>
					</div>
				</div>`
            $('#report').append(html);
        });
    }

    function generate_full_text_highlights(text_A, text_B, data){
        $('#report').append(`<br><h2>Full text highlights</h2>`);

        let new_A = text_A;
        let new_B = text_B;

        data.highlights.forEach(function(elem) {
            new_A = new_A.slice(0, elem.textA.startIndex) + '<mark>' +
                text_A.slice(elem.textA.startIndex, elem.textA.endIndex) +
                '</mark>' + text_A.slice(elem.textA.endIndex, text_B.lenght);
            new_B = new_B.slice(0, elem.textB.startIndex) + '<mark>' +
                text_B.slice(elem.textB.startIndex, elem.textB.endIndex) +
                '</mark>' + text_B.slice(elem.textB.endIndex, text_A.lenght);
        });

        let html = `
			<div class="card" style="margin-top: 20px;">
	            <div class="card-body">
	                <div class="row align-items-start">
	                    <div class="col-sm">
	                    	${new_A}
	                    </div>
	                    <div class="col-sm">
	                    	${new_B}
	                    </div>
	                </div>
	                <br>
	            </div>
	        </div>`
        $('#report').append(html);
    }


    function generate_report(text_A, text_B, data) {

        generate_main_progres_bar(data);
        if (data.highlights.length > 0) {
            generate_hight_batches(text_A, text_B, data);
            generate_full_text_highlights(text_A, text_B, data);
        }
    }

    function make_request(text_A, text_B){
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/plagiarism', // SET URL HERE
            dataType: 'json',
            data: JSON.stringify({
                text_a: text_A,
                text_b: text_B
            }),
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Content-Type', 'application/json')
            },
            success: function (data) {
                generate_report(text_A, text_B, data);
                $('#loading').attr('style', 'display:none !important');
                $('#generate-button-div').show();
            }
        });
    }


    $('#generate-button').click(function() {
        let text_A = $('#floatingTextarea1').val();
        let text_B = $('#floatingTextarea2').val();

        if (text_A.length > 0 && text_B.length> 0) {
            $('#report').html('');
            // delete generate button
            $('#generate-button-div').attr('style', 'display:none !important');
            //show loading bar
            $('#loading').show();
            make_request(text_A, text_B);
        }

    });
});