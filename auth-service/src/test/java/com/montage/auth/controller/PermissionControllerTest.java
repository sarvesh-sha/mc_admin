package com.montage.auth.controller;

//@WebMvcTest(PermissionController.class)
class PermissionControllerTest {
    
//    @Autowired
//    private MockMvc mockMvc;
//    
//    @MockBean
//    private PermissionService permissionService;
//    
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void createPermission_Success() throws Exception {
//        // Arrange
//        PermissionDTO dto = new PermissionDTO();
//        dto.setName("TEST_PERMISSION");
//        
//        when(permissionService.createPermission(any())).thenReturn(dto);
//        
//        // Act & Assert
//        mockMvc.perform(post("/api/permissions")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{\"name\":\"TEST_PERMISSION\"}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("TEST_PERMISSION"));
//    }
} 