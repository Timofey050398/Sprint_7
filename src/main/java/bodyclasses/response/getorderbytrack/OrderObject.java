package bodyclasses.response.getorderbytrack;

import java.util.List;

public class OrderObject {
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        private String firstName;
        private String lastName;
        private String address;
        private String metroStation;
        private String phone;
        private int rentTime;
        private String deliveryDate;
        private int track;
        private int status;
        private List<String> color;
        private String comment;
        private boolean cancelled;
        private boolean finished;
        private boolean inDelivery;
        private String courierFirstName;
        private String createdAt;
        private String updatedAt;

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isInDelivery() {
        return inDelivery;
    }

    public void setInDelivery(boolean inDelivery) {
        this.inDelivery = inDelivery;
    }

    public String getCourierFirstName() {
        return courierFirstName;
    }

    public void setCourierFirstName(String courierFirstName) {
        this.courierFirstName = courierFirstName;
    }


    public int getTrack() {
            return track;
        }

        public void setTrack(int track) {
            this.track = track;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }


        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getMetroStation() {
            return metroStation;
        }

        public void setMetroStation(String metroStation) {
            this.metroStation = metroStation;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getRentTime() {
            return rentTime;
        }

        public void setRentTime(int rentTime) {
            this.rentTime = rentTime;
        }

        public String getDeliveryDate() {
            return deliveryDate;
        }

        public void setDeliveryDate(String deliveryDate) {
            this.deliveryDate = deliveryDate;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public List<String> getColor() {
            return color;
        }

        public void setColor(List<String> color) {
            this.color = color;
        }
}
