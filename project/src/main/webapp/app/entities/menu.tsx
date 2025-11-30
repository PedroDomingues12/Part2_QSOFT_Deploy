import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/ingredient">
        <Translate contentKey="global.menu.entities.ingredient" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/dish">
        <Translate contentKey="global.menu.entities.dish" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/menu">
        <Translate contentKey="global.menu.entities.menu" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/menu-dishes">
        <Translate contentKey="global.menu.entities.menuDishes" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/purchase">
        <Translate contentKey="global.menu.entities.purchase" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
