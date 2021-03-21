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
						style="width: ${plagiarismPercent}%;" aria-valuenow="${plagiarismPercent}" 
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

	function generate_fuul_text_highlights(text_A, text_B, data){
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
		$('#report').html('');
		generate_main_progres_bar(data);
		generate_hight_batches(text_A, text_B, data);
		generate_fuul_text_highlights(text_A, text_B, data);
	}

	function make_request(text_A, text_B){
		$.ajax({
                type: 'GET',
                url: '', // SET URL HERE
                dataType: 'json',
                data: JSON.stringify({
                    text_A: text_A,
                    text_B: text_B
                }),
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('Content-Type', 'application/json')
                },
                success: function (response) {
                    generate_report(text_A, text_B, data);
                }
            });
	}


	$('#generate-button').click(function() {
		// delete generate button
		$('#generate-button-div').attr('style', 'display:none !important');
		//show loading bar
		$('#loading').show();

		let text_A = $(this).find('[name=textA]').val();
        let text_B = $(this).find('[name=textB]').val();

        // make_request(text_A, text_B);

        // REGUEST MINI MOCK
		generate_report(
			`Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam sagittis, orci id tristique efficitur, eros risus semper sapien, quis semper erat nunc tincidunt justo. Nunc sit amet neque quis urna dignissim accumsan. Phasellus ut laoreet ipsum. Nulla suscipit ante ut rutrum fermentum. Pellentesque sed blandit risus. Quisque in tempor tellus. Curabitur feugiat pretium est non feugiat. Duis sit amet sagittis nunc. Nullam in quam eu neque accumsan sodales. In interdum lorem id risus eleifend vulputate. Maecenas ullamcorper congue porta. Mauris enim mi, vehicula ac nulla sit amet, commodo rhoncus orci. Curabitur tortor nunc, bibendum at rutrum at, ornare sed ligula. Donec porta ipsum massa, ac ultricies dolor venenatis at. Praesent at libero velit.`,
			`Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam sagittis, orci id tristique efficitur, eros risus semper sapien, quis semper erat nunc tincidunt justo. Nunc sit amet neque quis urna dignissim accumsan. Phasellus ut laoreet ipsum. Nulla suscipit ante ut rutrum fermentum. Pellentesque sed blandit risus. Quisque in tempor tellus. Curabitur feugiat pretium est non feugiat. Duis sit amet sagittis nunc. Nullam in quam eu neque accumsan sodales. In interdum lorem id risus eleifend vulputate. Maecenas ullamcorper congue porta. Mauris enim mi, vehicula ac nulla sit amet, commodo rhoncus orci. Curabitur tortor nunc, bibendum at rutrum at, ornare sed ligula. Donec porta ipsum massa, ac ultricies dolor venenatis at. Praesent at libero velit.`,
			{
				plagiarismLevel: 0.3742432236671448,
				highlights: [
					{
						textA: {
							startIndex: 0,
							endIndex: 200
						},
						textB: {
							startIndex: 0,
							endIndex: 200
						},
						plagiarismLevel: 0.9999898672103882
					},
					{
						textA: {
							startIndex: 301,
							endIndex: 500
						},
						textB: {
							startIndex: 300,
							endIndex: 500
						},
						plagiarismLevel: 0.7599898672103882
					}
				]

			}
		);

		$('#loading').attr('style', 'display:none !important');
		$('#generate-button-div').show();

});
});