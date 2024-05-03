package ru.tolstov.services.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.tolstov.services.CatService;
import ru.tolstov.services.OwnerService;

@AllArgsConstructor
@Component
public class CustomPreAuthorizeService {
    private CatService catService;
    private OwnerService ownerService;
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

    public boolean oneOfFriends(Authentication auth, Long catId) {
        var user = (UserDetailsImpl) auth.getPrincipal();
        Long ownerId = user.getUser().getOwner();
        for (var cat : ownerService.getAllCats(ownerId))
            if (cat.getFriends().contains(catId))
                return true;

        return false;
    }
}
