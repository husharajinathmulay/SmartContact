const search = () => {
	let query = $("#search-input").val();

	if (query == "") {
		$(".search-result").hide();
	} else {
		$(".search-result").show();
		let url = `http://localhost:3332/search/${query}`;
		fetch(url).then((response) => {
			return response.json();
		}).then((data) => {
			console.log(data);
			let text = `<div class='list-group'>`;

			data.forEach((contact) => {
				text += `<a href='/user/contact-details/${contact.id}' class='list-group-item list-group-action'>${contact.name}</a>`
			});
			text += `</div>`;
			$(".search-result").html(text);
		});

	}
};
const paymentstart = () => {
	let amount = $("#amount").val();
	if (amount == '' || amount == null) {
		swal("failed!", "Amount is requird!", "error");
	}
	$.ajax({
		url: '/user/create-order',
		data: JSON.stringify({ amount: amount, info: 'other_request' }),
		contentType: 'application/json',
		type: 'post',
		dataType: 'json',
		success: function(response) {
			if (response.status == "created") {
				let options = {
					"key": "rzp_test_Wn2lcwFTeCfzXU",
					"amount": response.amount,
					"currency": "INR",
					"name": "Smart Contact Manager donation",
					"description": "Payment",
					"image": "https://uni-access.com/FrontMedia/images/logo.png",
					"order_id":response.id,
					"handler": function (response){
						console.log(response.razorpay_payment_id);
						console.log(response.razorpay_order_id);
						console.log(response.razorpay_signature);
						console.log("payment successfull");
						updatePaymentOnServer(response.razorpay_payment_id,response.razorpay_order_id,"paid");
						
						}
				};
				let rzp=new Razorpay(options);
				rzp.open();
				
			}
		},
		
		//error: function(error) {
		//	alert('something went wrong');
		//}

	})
};

const toggleSidebar = () => {
	if ($('.sidebar').is(":visible")) {
		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "0%");
	}else{
		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "20%")
	}
};

function updatePaymentOnServer(payment_id,order_id,status)
{
$.ajax({
		url: '/user/payment-success',
		data: JSON.stringify({ payment_id: payment_id, order_id:order_id,status:status}),
		contentType: 'application/json',
		type: 'post',
		dataType: 'json',
		success: function(response) {
			swal("Good job!", "Congratulation payment successfully", "success");
		},
		error:function(error){
		swal("Failed !", "your payment successfull but not get on server ", "error");	
		},
	})
	}