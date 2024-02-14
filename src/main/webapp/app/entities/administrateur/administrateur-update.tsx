import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IAdministrateur } from 'app/shared/model/administrateur.model';
import { getEntity, updateEntity, createEntity, reset } from './administrateur.reducer';

export const AdministrateurUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const administrateurEntity = useAppSelector(state => state.administrateur.entity);
  const loading = useAppSelector(state => state.administrateur.loading);
  const updating = useAppSelector(state => state.administrateur.updating);
  const updateSuccess = useAppSelector(state => state.administrateur.updateSuccess);

  const handleClose = () => {
    navigate('/administrateur');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...administrateurEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...administrateurEntity,
          user: administrateurEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="isimmManagerApp.administrateur.home.createOrEditLabel" data-cy="AdministrateurCreateUpdateHeading">
            <Translate contentKey="isimmManagerApp.administrateur.home.createOrEditLabel">Create or edit a Administrateur</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="administrateur-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('isimmManagerApp.administrateur.prenom')}
                id="administrateur-prenom"
                name="prenom"
                data-cy="prenom"
                type="text"
              />
              <ValidatedField
                label={translate('isimmManagerApp.administrateur.nom')}
                id="administrateur-nom"
                name="nom"
                data-cy="nom"
                type="text"
              />
              <ValidatedField
                label={translate('isimmManagerApp.administrateur.email')}
                id="administrateur-email"
                name="email"
                data-cy="email"
                type="text"
              />
              <ValidatedField
                label={translate('isimmManagerApp.administrateur.matricule')}
                id="administrateur-matricule"
                name="matricule"
                data-cy="matricule"
                type="text"
              />
              <ValidatedField
                label={translate('isimmManagerApp.administrateur.titre')}
                id="administrateur-titre"
                name="titre"
                data-cy="titre"
                type="text"
              />
              <ValidatedField
                label={translate('isimmManagerApp.administrateur.numTel')}
                id="administrateur-numTel"
                name="numTel"
                data-cy="numTel"
                type="text"
              />
              <ValidatedField
                label={translate('isimmManagerApp.administrateur.dateDeNaissance')}
                id="administrateur-dateDeNaissance"
                name="dateDeNaissance"
                data-cy="dateDeNaissance"
                type="date"
              />
              <ValidatedBlobField
                label={translate('isimmManagerApp.administrateur.photoDeProfile')}
                id="administrateur-photoDeProfile"
                name="photoDeProfile"
                data-cy="photoDeProfile"
                isImage
                accept="image/*"
              />
              <ValidatedField
                id="administrateur-user"
                name="user"
                data-cy="user"
                label={translate('isimmManagerApp.administrateur.user')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/administrateur" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default AdministrateurUpdate;
