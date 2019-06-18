package gr.uoa.di.rent.controllers;

import gr.uoa.di.rent.models.Business;
import gr.uoa.di.rent.models.Hotel;
import gr.uoa.di.rent.payload.requests.ProviderApplicationRequest;
import gr.uoa.di.rent.payload.requests.filters.PagedResponseFilter;
import gr.uoa.di.rent.payload.responses.PagedResponse;
import gr.uoa.di.rent.repositories.BusinessRepository;
import gr.uoa.di.rent.security.CurrentUser;
import gr.uoa.di.rent.security.Principal;
import gr.uoa.di.rent.util.AppConstants;
import gr.uoa.di.rent.util.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/businesses")
public class BusinessController {

    private static final Logger logger = LoggerFactory.getLogger(BusinessController.class);

    private final BusinessRepository businessRepository;

    public BusinessController(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }

    @GetMapping("")
    @PreAuthorize("hasRole('PROVIDER') or hasRole('ADMIN')")
    List<Business> findAll() {
        return businessRepository.findAll();
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('PROVIDER') or hasRole('ADMIN')")
    public PagedResponse<Business> findAllByProviderId(@Valid PagedResponseFilter filters, @CurrentUser Principal principal) {
        Page<Business> businesses;
        Sort.Direction sort_order;

        /* Default order is ASC, otherwise DESC */
        if (AppConstants.DEFAULT_ORDER.equals(filters.getOrder())) {
            sort_order = Sort.Direction.ASC;
        } else {
            sort_order = Sort.Direction.DESC;
        }

        Pageable pageable = PageRequest.of(filters.getPage(), filters.getSize(), sort_order, filters.getSort_field());

        businesses = businessRepository.findAllByProviderId(pageable, principal.getUser().getId());

        if (businesses.getNumberOfElements() == 0) {
            return new PagedResponse<>(
                    Collections.emptyList(),
                    businesses.getNumber(),
                    businesses.getSize(),
                    businesses.getTotalElements(),
                    businesses.getTotalPages(),
                    businesses.isLast()
            );
        }

        List<Business> businessResponses = businesses.map(ModelMapper::mapBusinessToBusinessResponse).getContent();

        return new PagedResponse<>(businessResponses, businesses.getNumber(),
                businesses.getSize(), businesses.getTotalElements(), businesses.getTotalPages(), businesses.isLast());
    }

    @PostMapping("")
    @PreAuthorize("hasRole('PROVIDER') or hasRole('ADMIN')")
    Business newBusiness(@CurrentUser Principal principal,
                         @Valid @RequestBody ProviderApplicationRequest providerApplicationRequest) {
        return businessRepository.save(new Business(
                providerApplicationRequest.getCompany_name(),
                providerApplicationRequest.getEmail(),
                providerApplicationRequest.getCompany_address(),
                providerApplicationRequest.getTax_number(),
                providerApplicationRequest.getTax_office(),
                providerApplicationRequest.getOwner_name(),
                providerApplicationRequest.getOwner_surname(),
                providerApplicationRequest.getOwner_patronym(),
                providerApplicationRequest.getId_card_number(),
                providerApplicationRequest.getId_card_date_of_issue(),
                providerApplicationRequest.getResidence_address(),
                principal.getUser(),
                null
        ));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('PROVIDER') or hasRole('ADMIN')")
    Business findOne(@PathVariable @Min(1) Long id) {
        return businessRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Business [" + id + "] not found!")
                );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PROVIDER') or hasRole('ADMIN')")
    Business saveOrUpdate(@RequestBody Business newBusiness, @PathVariable Long id) {
        return businessRepository.findById(id)
                .map(x -> businessRepository.save(x))
                .orElseGet(() -> businessRepository.save(newBusiness));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('PROVIDER') or hasRole('ADMIN')")
    Business patch(@RequestBody Map<String, String> update, @PathVariable Long id) {
        return businessRepository.findById(id)
                .map(x -> {
                    String business_name = update.get("business_name");
                    if (!StringUtils.isEmpty(business_name)) {
                        x.setBusiness_name(business_name);
                        return businessRepository.save(x);
                    } else {
                        throw new RuntimeException("Field " + update.keySet() + " update is not allow.");
                    }

                })
                .orElseGet(() -> {
                    throw new RuntimeException("Business [" + id + "] not found!");
                });
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROVIDER') or hasRole('ADMIN')")
    void deleteBusiness(@PathVariable Long id) {
        businessRepository.deleteById(id);
    }
}
