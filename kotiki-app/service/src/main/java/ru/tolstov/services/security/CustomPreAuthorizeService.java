package ru.tolstov.services.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.tolstov.services.CatService;

@AllArgsConstructor
@Component
public class CustomPreAuthorizeService {
    private CatService catService;
    public boolean hasAccessToCat(Authentication auth, Long catId) {
        var user = (UserDetailsImpl) auth.getPrincipal();
        var cat = catService.getCatByID(catId);
        if (cat.isEmpty())
            return false;

        return cat.get().getOwner().equals(user.getUser().getOwner());
    }

    public boolean isCurrentOwner(Authentication auth, Long ownerId) {
        var user = (UserDetailsImpl) auth.getPrincipal();
        return user.getUser().getOwner().equals(ownerId);
    }
}
