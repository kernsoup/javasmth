**Renata Averianova**

try_login() - asks user if they are registered in hotel's system

login() - initiates login

register() - initiates registration

create_username() - creating username

create_password() - creating password()

not_found() - is performed when username is not found when user is trying to log in

logged_in(String user) - menu of logged in user; allows to check on user's bookings, kame a new bookinr, or log out

log out() - logs out

my_bookings(int type, String user) - allows to find bookings by specific criteria; basic user can only see their bookings, admin can see all the bookings, find bookings by reservation number, username, or reservant's name

choosing_type(String user) - user chooses between single or double rooms 

booking(String user, int type) - checks how many overall bookings user has; if more than 2, the price for 1 night is reducted by 10%, more than 4 - by 20%. Having more than 6 bookings is not allowed. Asks for reservant's name, shows total price, and asks for confirmation. 

overwrite (String filename, String content) - if booking is confirmed, adds a line to bookings.txt file about the booking and marks the chosen room in rooms.txt as unavailable. 

number_of_bookings(String user) - calculates how namy booings user has

admin() - admin's menu. Admin can't make a reservation, but can see other's reservation (see my_bookings)


bookings.txt - file with all the bookings. in each line there is reservation number, number of room, username, number of days and booker's name

rooms.txt - file with room numbers. a dash near number means it's taken

user_data.txt - username and passwords
