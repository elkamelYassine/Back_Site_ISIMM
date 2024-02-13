import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IEtudiant } from 'app/shared/model/etudiant.model';
import { getEntities as getEtudiants } from 'app/entities/etudiant/etudiant.reducer';
import { IFichierAdmin } from 'app/shared/model/fichier-admin.model';
import { TypeFichierAdmin } from 'app/shared/model/enumerations/type-fichier-admin.model';
import { getEntity, updateEntity, createEntity, reset } from './fichier-admin.reducer';

export const FichierAdminUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const etudiants = useAppSelector(state => state.etudiant.entities);
  const fichierAdminEntity = useAppSelector(state => state.fichierAdmin.entity);
  const loading = useAppSelector(state => state.fichierAdmin.loading);
  const updating = useAppSelector(state => state.fichierAdmin.updating);
  const updateSuccess = useAppSelector(state => state.fichierAdmin.updateSuccess);
  const typeFichierAdminValues = Object.keys(TypeFichierAdmin);

  const handleClose = () => {
    navigate('/fichier-admin');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getEtudiants({}));
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
      ...fichierAdminEntity,
      ...values,
      etudiant: etudiants.find(it => it.id.toString() === values.etudiant.toString()),
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
          type: 'Presence',
          ...fichierAdminEntity,
          etudiant: fichierAdminEntity?.etudiant?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="isimmManagerApp.fichierAdmin.home.createOrEditLabel" data-cy="FichierAdminCreateUpdateHeading">
            <Translate contentKey="isimmManagerApp.fichierAdmin.home.createOrEditLabel">Create or edit a FichierAdmin</Translate>
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
                  id="fichier-admin-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('isimmManagerApp.fichierAdmin.titre')}
                id="fichier-admin-titre"
                name="titre"
                data-cy="titre"
                type="text"
              />
              <ValidatedField
                label={translate('isimmManagerApp.fichierAdmin.type')}
                id="fichier-admin-type"
                name="type"
                data-cy="type"
                type="select"
              >
                {typeFichierAdminValues.map(typeFichierAdmin => (
                  <option value={typeFichierAdmin} key={typeFichierAdmin}>
                    {translate('isimmManagerApp.TypeFichierAdmin.' + typeFichierAdmin)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedBlobField
                label={translate('isimmManagerApp.fichierAdmin.file')}
                id="fichier-admin-file"
                name="file"
                data-cy="file"
                openActionLabel={translate('entity.action.open')}
              />
              <ValidatedField
                label={translate('isimmManagerApp.fichierAdmin.demandeValide')}
                id="fichier-admin-demandeValide"
                name="demandeValide"
                data-cy="demandeValide"
                check
                type="checkbox"
              />
              <ValidatedField
                id="fichier-admin-etudiant"
                name="etudiant"
                data-cy="etudiant"
                label={translate('isimmManagerApp.fichierAdmin.etudiant')}
                type="select"
              >
                <option value="" key="0" />
                {etudiants
                  ? etudiants.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/fichier-admin" replace color="info">
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

export default FichierAdminUpdate;
