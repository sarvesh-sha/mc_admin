package com.montage.device.entity;

import com.montage.device.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import java.time.LocalDateTime;

@Entity
@Table(name = "installation_history")
@Audited
@Getter
@Setter
public class InstallationHistory extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String status;
    
    @Column(name = "installed_by")
    private String installedBy;
    
    @Column(name = "installation_finish_date")
    private LocalDateTime installationFinishDate;
    
    @Column(name = "installation_code")
    private String installationCode;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;
} 