# Backend Technical Test

Please create a restful webservice with any framework you are familiar with.

## Backend

Java (11 or newer).
A single controller that has endpoints for each of the following operations:

- Create a booking
- Read a booking
- Update a booking
- Delete a booking

The controller should allow for creating a booking and a block. Bookings can be canceled and rebooked. Blocks can be created and deleted.
Have some logic in place to prevent double (overlapping) bookings.

## Database

Please use in-memory volatile DB.

## Terminology

A booking is when a guest selects a start and end date and submits a reservation on a property.
A block is when the property owner selects a start and end date where no one can make a booking on the dates within the date range.
