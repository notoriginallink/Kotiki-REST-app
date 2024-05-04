package ru.tolstov.services.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.tolstov.repositories.CatRepository;
import ru.tolstov.repositories.OwnerRepository;
import ru.tolstov.services.dto.CatDto;

@AllArgsConstructor
@Component("authorizeService")
public class CustomAuthorizeService {
    private CatRepository catRepository;
    private OwnerRepository ownerRepository;
    /**
    * Checks if current user has access to perform operations with cat
    * @param cat CatDto object
     * @return true if cat is null or if cat belongs to current user. false otherwise
    * **/
    public boolean hasAccessToCat(Authentication auth, CatDto cat) {
        var user = (UserDetailsImpl) auth.getPrincipal();
        if (cat == null)
            return true;

        return cat.getOwner().equals(user.getUser().getOwner());
    }

    /**
     * Checks if current user has access to perform operations with cat
     * @param catId cat's ID
     * @return true if cat with this ID is not present in database or if cat belongs to current user. false otherwise
     * **/
    public boolean hasAccessToCatID(Authentication auth, long catId) {
        var user = (UserDetailsImpl) auth.getPrincipal();
        var cat = catRepository.findById(catId);

        if (cat.isEmpty())
            return true;
        System.out.println(cat.get());

        return cat.get().getOwner().getId().equals(user.getUser().getOwner());
    }

    /**
     * Checks if current user has this owner ID
     * @param ownerId owner's ID
     * @return true if so, false otherwise
     * **/
    public boolean isCurrentOwner(Authentication auth, long ownerId) {
        var user = (UserDetailsImpl) auth.getPrincipal();
        return user.getUser().getOwner().equals(ownerId);
    }

    /**
     * Checks if one of current user's cats is in friendship with passed cat
     * @param cat CatDto object
     * @return true if at least one of current user's cats is in friendship with that cat
     * **/
    public boolean isOneOfFriends(Authentication auth, CatDto cat) {
        var user = (UserDetailsImpl) auth.getPrincipal();
        Long ownerId = user.getUser().getOwner();
        for (var c : ownerRepository.findById(ownerId).get().getCats())
            for (var friend : c.getFriends())
                if (friend.getId() == cat.getId())
                    return true;

        return false;
    }
}
