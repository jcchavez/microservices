package com.ioSoftware.customerservice.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_customers")
public class Customer {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotEmpty(message = "Number ID cannot be empty")
        @Size(min = 8, max = 8, message = "The Number ID must be 8 characters")
        @Column(name = "number_id", unique = true, length = 8, nullable = false)
        private String numberId;

        @NotEmpty(message = "First name cannot be empty")
        @Column(name = "first_name", nullable = false)
        private String firstName;

        @NotEmpty(message = "Last name cannot be empty")
        @Column(name = "last_name", nullable = false)
        private String lastName;

        @NotEmpty(message = "Email cannot be null")
        @Email(message = "Email format incorrect")
        @Column(unique = true, nullable = false)
        private String email;

        @Column(name = "photo_url")
        private String photoUrl;

        @NotNull(message = "Region cannot be empty")
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "region_id")
        @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
        private Region region;


        private String state;
}
