package com.montage.auth.service;

//@ExtendWith(MockitoExtension.class)
class PermissionServiceTest {
//    
//    @Mock
//    private PermissionRepository permissionRepository;
//    
//    @Mock
//    private PermissionMapper permissionMapper;
//    
//    @InjectMocks
//    private PermissionServiceImpl permissionService;
//    
//    @Test
//    void createPermission_Success() {
//        // Arrange
//        PermissionDTO dto = new PermissionDTO();
//        dto.setName("TEST_PERMISSION");
//        
//        Permission entity = new Permission();
//        entity.setName("TEST_PERMISSION");
//        
//        when(permissionRepository.existsByName(dto.getName())).thenReturn(false);
//        when(permissionMapper.toEntity(dto)).thenReturn(entity);
//        when(permissionRepository.save(entity)).thenReturn(entity);
//        when(permissionMapper.toDTO(entity)).thenReturn(dto);
//        
//        // Act
//        PermissionDTO result = permissionService.createPermission(dto);
//        
//        // Assert
//        assertNotNull(result);
//        assertEquals(dto.getName(), result.getName());
//        verify(permissionRepository).save(entity);
//    }
//    
//    @Test
//    void createPermission_DuplicateName_ThrowsException() {
//        // Arrange
//        PermissionDTO dto = new PermissionDTO();
//        dto.setName("TEST_PERMISSION");
//        
//        when(permissionRepository.existsByName(dto.getName())).thenReturn(true);
//        
//        // Act & Assert
//        assertThrows(IllegalArgumentException.class, 
//            () -> permissionService.createPermission(dto));
//    }
} 