//package com.ducbao.service_be.mapping;
//
//import com.ducbao.service_be.model.entity.UserModel;
//import com.ducbao.service_be.repository.UserRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.modelmapper.Converter;
//import org.modelmapper.ModelMapper;
//import org.modelmapper.spi.MappingContext;
//
//@Slf4j
//public class AccountIDToShopConverter implements Converter<String, String> {
//    private final UserRepository userRepository;
//
//    public AccountIDToShopConverter(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public String convert(MappingContext<String, String> mappingContext) {
//        if (mappingContext.getSource() == null) {
//            return null;
//        }
//
//        try {
//            UserModel userModel = userRepository.findById(mappingContext.getSource()).get();
//            if (userModel == null) {
//                return null;
//            }
//            return userModel.getId();
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return null;
//        }
//    }
//}
