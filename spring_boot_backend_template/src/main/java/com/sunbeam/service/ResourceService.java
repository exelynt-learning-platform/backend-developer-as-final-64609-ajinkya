package com.sunbeam.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunbeam.dto.ResourceDto;
import com.sunbeam.entity.Resource;
import com.sunbeam.repository.ResourceRepository;

@Service
@Transactional
public class ResourceService {

    private final ResourceRepository resourceRepository;

    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }

    public Resource getResourceById(Long id) {
        return resourceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Resource not found with id: " + id));
    }

    public Resource createResource(ResourceDto dto) {
        Resource resource = new Resource();
        resource.setName(dto.getName());
        resource.setType(dto.getType());
        resource.setLocation(dto.getLocation());
        resource.setCapacity(dto.getCapacity());
        resource.setPrice(dto.getPrice());
        resource.setDescription(dto.getDescription());
        return resourceRepository.save(resource);
    }

    public Resource updateResource(Long id, ResourceDto dto) {
        Resource resource = getResourceById(id);
        resource.setName(dto.getName());
        resource.setType(dto.getType());
        resource.setLocation(dto.getLocation());
        resource.setCapacity(dto.getCapacity());
        resource.setPrice(dto.getPrice());
        resource.setDescription(dto.getDescription());
        return resourceRepository.save(resource);
    }

    public void deleteResource(Long id) {
        Resource resource = getResourceById(id);
        resourceRepository.delete(resource);
    }
}
