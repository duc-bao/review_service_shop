//package com.ducbao.service_be.config.mapper;
//
//import com.ducbao.service_be.mapping.AccountIDToShopConverter;
//import com.ducbao.service_be.model.entity.ShopModel;
//import com.ducbao.service_be.model.entity.UserModel;
//import com.ducbao.service_be.repository.UserRepository;
//import org.modelmapper.PropertyMap;
//
//public class UserModelToShopModel extends PropertyMap<UserModel, ShopModel> {
//    private final UserRepository userRepository;
//    public UserModelToShopModel(final UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    protected void configure() {
//        using(new AccountIDToShopConverter(userRepository)).map(source.getId()).setIdUser(null);
//    }
//}
