# Campsite Reservation API Repository

# Reservation REST API:
- GET http://localhost:8080/available-dates
- GET http://localhost:8080/reservations/{id}
- POST http://localhost:8080/reservation
- PUT http://localhost:8080/reservations/{id}
- DELETE http://localhost:8080/reservations/{id}

# Reservation Constraints:
- The campsite will be free for all.
- The campsite can be reserved for max 3 days.
- The campsite can be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance. Reservations can be cancelled anytime.
- For sake of simplicity assume the check-in & check-out time is 12:00 AM

# System Requirements
- The users will need to find out when the campsite is available. So the system should expose an API to provide information of the availability of the campsite for a given date range with the default being 1 month.
- Provide an end point for reserving the campsite. The user will provide his/her email & full name at the time of reserving the campsite along with intended arrival date and departure date. Return a unique booking identifier back to the caller if the reservation is successful. 
- The unique booking identifier can be used to modify or cancel the reservation later on. Provide appropriate end point(s) to allow modification/cancellation of an existing reservation
- Due to the popularity of the island, there is a high likelihood of multiple users attempting to reserve the campsite for the same/overlapping date(s). Demonstrate with appropriate test cases that the system can gracefully handle concurrent requests to reserve the campsite. 
- Provide appropriate error messages to the caller to indicate the error cases.
- In general, the system should be able to handle large volume of requests for getting the campsite availability.
There are no restrictions on how reservations are stored as as long as system constraints are not violated.

# Examples:
- GET http://localhost:8080/available-dates
<img width="1315" alt="Screen Shot 2022-10-16 at 11 22 10 AM" src="https://user-images.githubusercontent.com/17466715/196043669-9f9fe8bd-0e6d-4377-a75a-5a31089cc2d1.png">

- GET http://localhost:8080/reservations/{id}
<img width="1263" alt="Screen Shot 2022-10-16 at 11 23 02 AM" src="https://user-images.githubusercontent.com/17466715/196043707-ae37d4fa-35c9-4664-84ff-df5ba373893b.png">

- POST http://localhost:8080/reservation
<img width="814" alt="Screen Shot 2022-10-13 at 12 32 07 PM" src="https://user-images.githubusercontent.com/17466715/196043731-8b44b7b2-3147-4cff-b081-a62c2d534d6b.png">

<img width="1130" alt="Screen Shot 2022-10-13 at 12 32 27 PM" src="https://user-images.githubusercontent.com/17466715/196043732-a348995a-7da4-41fb-bb8a-9dd53cb94b84.png">

- PUT http://localhost:8080/reservations/{id}
<img width="1206" alt="Screen Shot 2022-10-16 at 11 24 09 AM" src="https://user-images.githubusercontent.com/17466715/196043775-153703ff-bb29-4b64-acdf-e00d5385e0c1.png">

- DELETE http://localhost:8080/reservations/{id}
<img width="1165" alt="Screen Shot 2022-10-16 at 11 24 54 AM" src="https://user-images.githubusercontent.com/17466715/196043789-61cb5507-76ad-4ec5-8846-5b387a696d9f.png">
