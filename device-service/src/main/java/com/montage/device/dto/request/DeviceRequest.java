package com.montage.device.dto.request;

import java.time.LocalDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DeviceRequest {
    @NotBlank(message = "Device type is required")
    private String deviceType;

    @NotBlank(message = "IMEI is required")
    @Pattern(regexp = "^[0-9]{15}$", message = "IMEI must be 15 digits")
    private String imei;

  //  @NotBlank(message = "Make is required")
    private String make;

   // @NotBlank(message = "Model is required")
    private String model;

  //  @NotBlank(message = "Status is required")
    private String status;

  //  @NotNull(message = "isActive flag is required")
    private Boolean isActive;

   // @Future(message = "Installation date must be in the future")
    private LocalDateTime installationDate;

   // @NotNull(message = "Config version is required")
   // @Min(value = 0, message = "Config version must be non-negative")
    private Integer configVersion;

   // @NotNull(message = "Firmware version is required")
   // @Min(value = 0, message = "Firmware version must be non-negative")
    private Integer firmwareVersion;

   // @NotNull(message = "Bootloader version is required")
   // @Min(value = 0, message = "Bootloader version must be non-negative")
    private Integer bootloaderVersion;

   // @NotNull(message = "Health value is required")
   // @Min(value = 0, message = "Health value must be non-negative")
    private Integer healthValue;

  //  @NotNull(message = "Hardware version is required")
   // @Min(value = 0, message = "Hardware version must be non-negative")
    private Integer hardwareVersion;

   // @NotNull(message = "Protocol version is required")
  //  @Min(value = 0, message = "Protocol version must be non-negative")
    private Integer protocolVersion;

   // @NotBlank(message = "Asset name is required")
    private String assetName;

   // @NotNull(message = "Serial number is required")
   // @Min(value = 1, message = "Serial number must be greater than 0")
    private Integer serialNumber;

   // @Valid
   // @NotNull(message = "Customer is required")
    private Integer customerId;

    //@Valid
    @NotNull(message = "OTA group is required")
    private Integer otaGroupId;

   // @Valid
    //@NotNull(message = "Provisioning status is required")
    private Integer provisioningStatusId;

    //@Valid
   // @NotNull(message = "Product is required")
    private Integer productId;

    //@NotNull(message = "ICCID is required")
    private Integer iccid;
} 