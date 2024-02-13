import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/etudiant">
        <Translate contentKey="global.menu.entities.etudiant" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/administrateur">
        <Translate contentKey="global.menu.entities.administrateur" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/professeur">
        <Translate contentKey="global.menu.entities.professeur" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/niveau">
        <Translate contentKey="global.menu.entities.niveau" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/fichier-admin">
        <Translate contentKey="global.menu.entities.fichierAdmin" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/actulaite">
        <Translate contentKey="global.menu.entities.actulaite" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/note">
        <Translate contentKey="global.menu.entities.note" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/club">
        <Translate contentKey="global.menu.entities.club" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/seance">
        <Translate contentKey="global.menu.entities.seance" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/matiere">
        <Translate contentKey="global.menu.entities.matiere" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/cours">
        <Translate contentKey="global.menu.entities.cours" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/semestre">
        <Translate contentKey="global.menu.entities.semestre" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
