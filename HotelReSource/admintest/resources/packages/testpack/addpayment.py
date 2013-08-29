from cutil import printVar
from com.gwthotel.hotel.server.service import H
from com.gwthotel.hotel.reservation import ReservationPaymentDetail
from com.gwthotel.admintest.guice import ServiceInjector
from com.gwthotel.hotel.customer import HotelCustomer
from cutil import toDate
import datetime
from cutil import toB

def dialogaction(action,var) :
     printVar("doaction",action,var)

     if action == "addpayment" :
         op = H.getResOp()
         custOp = H.getHotelCustomers()
         add = ReservationPaymentDetail()
         ho = ServiceInjector.getInstanceHotel()
         hins = ho.getHotel("AppInstanceTest","hotel")
         print hins
         cust = HotelCustomer()
         cust.setName("guest")
         custOp.addElem(hins,cust)
         
         add.setDescription("Beverage")
         add.setGuestName("guest")
         add.setPrice(toB(1))
         add.setPriceList(toB(155))
         # important: after decimal point !!!
         add.setPriceTotal(toB(1.1))
         add.setQuantity(2)
         add.setRoomName("P10")
         da = datetime.date(2013, 3, 2)
         add.setServDate(toDate(da))
         add.setVat("7%")
         op.addResAddPayment(hins, "2013/R/2", add)

         