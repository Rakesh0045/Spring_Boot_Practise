import React from "react";
// import Header from "./Header";

const HotelService = () => {
	return (
		<>
			<div className="mt-2">

				<div className="container shadow-lg rounded-4 mb-4">
					<div className="row">
						<h4 className="text-center">
							Services at <span className="text-primary"> Tasty Town </span>
							<span className="gap-2">
								<i className="bi bi-clock ml-3"></i> 24-Hour Customer Support
							</span>
						</h4>
					</div>

					<hr />

					<div className="container-fluid row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4 py-4">
						{/* WiFi */}
						<div className="col">
							<div className="card h-100">
								<div className="card-body">
									<h5 className="card-title hotel-color">
										<i className="bi bi-wifi"></i> Excellent food
									</h5>
									<p className="card-text">We offer our clients excellent quality services for many years, with the best and delicious food all over India.</p>
								</div>
							</div>
						</div>

						{/* Breakfast */}
						<div className="col">
							<div className="card h-100">
								<div className="card-body">
									<h5 className="card-title hotel-color">
										<i className="bi bi-cup-straw"></i> Fast food
									</h5>
									<p className="card-text">We offer our clients excellent quality services for many years, with the best and delicious food in Sri Lanka.</p>
								</div>
							</div>
						</div>

						{/* Laundry */}
						<div className="col">
							<div className="card h-100">
								<div className="card-body">
									<h5 className="card-title hotel-color">
										<i className="bi bi-truck"></i> Delivery
									</h5>
									<p className="card-text">We offer our clients excellent quality services for many years, with the best and delicious food in Sri Lanka.</p>
								</div>
							</div>
						</div>

						{/* <div className="col">
							<div className="card h-100">
								<div className="card-body">
									<h5 className="card-title hotel-color">
										<i className="bi bi-cup"></i> Mini-bar
									</h5>
									<p className="card-text">Enjoy a refreshing drink or snack from our in-room mini-bar.</p>
								</div>
							</div>
						</div>

						<div className="col">
							<div className="card h-100">
								<div className="card-body">
									<h5 className="card-title hotel-color">
										<i className="bi bi-car-front"></i> Parking
									</h5>
									<p className="card-text">Park your car conveniently in our on-site parking lot.</p>
								</div>
							</div>
						</div>


						<div className="col">
							<div className="card h-100">
								<div className="card-body">
									<h5 className="card-title hotel-color">
										<i className="bi bi-snow"></i> Air Conditioning
									</h5>
									<p className="card-text">Stay cool and comfortable with our air conditioning system.</p>
								</div>
							</div>
						</div> */}
					</div>

					{/* <hr /> */}
				</div>
			</div>
		</>
	);
};

export default HotelService;
