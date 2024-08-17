package com.ansbeno.books_service.domain.user;

import java.util.Arrays;
import java.util.List;

public enum Role {
      CUSTOMER(Arrays.asList(Permission.READ_ALL_PRODUCTS, Permission.SUBMIT_ONE_ORDER, Permission.READ_ORDERS)),
      ADMIN(Arrays.asList(
                  Permission.READ_ALL_PRODUCTS,
                  Permission.SAVE_ONE_PRODUCT,
                  Permission.DELETE_ONE_PRODUCT,
                  Permission.UPDATE_ONE_PRODUCT));

      private List<Permission> permissions;

      Role(List<Permission> permissions) {
            this.permissions = permissions;
      }

      public List<Permission> getPermissions() {
            return permissions;
      }

      void setPermissions(List<Permission> permissions) {
            this.permissions = permissions;
      }

}
