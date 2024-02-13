import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMatiere } from 'app/shared/model/matiere.model';
import { getEntities as getMatieres } from 'app/entities/matiere/matiere.reducer';
import { IProfesseur } from 'app/shared/model/professeur.model';
import { Departement } from 'app/shared/model/enumerations/departement.model';
import { getEntity, updateEntity, createEntity, reset } from './professeur.reducer';

export const ProfesseurUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const matieres = useAppSelector(state => state.matiere.entities);
  const professeurEntity = useAppSelector(state => state.professeur.entity);
  const loading = useAppSelector(state => state.professeur.loading);
  const updating = useAppSelector(state => state.professeur.updating);
  const updateSuccess = useAppSelector(state => state.professeur.updateSuccess);
  const departementValues = Object.keys(Departement);

  const handleClose = () => {
    navigate('/professeur');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getMatieres({}));
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
      ...professeurEntity,
      ...values,
      matieres: mapIdList(values.matieres),
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
          departement: 'Informatique',
          ...professeurEntity,
          matieres: professeurEntity?.matieres?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="isimmManagerApp.professeur.home.createOrEditLabel" data-cy="ProfesseurCreateUpdateHeading">
            <Translate contentKey="isimmManagerApp.professeur.home.createOrEditLabel">Create or edit a Professeur</Translate>
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
                  id="professeur-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('isimmManagerApp.professeur.prenom')}
                id="professeur-prenom"
                name="prenom"
                data-cy="prenom"
                type="text"
              />
              <ValidatedField
                label={translate('isimmManagerApp.professeur.nom')}
                id="professeur-nom"
                name="nom"
                data-cy="nom"
                type="text"
              />
              <ValidatedField
                label={translate('isimmManagerApp.professeur.email')}
                id="professeur-email"
                name="email"
                data-cy="email"
                type="text"
              />
              <ValidatedField
                label={translate('isimmManagerApp.professeur.matricule')}
                id="professeur-matricule"
                name="matricule"
                data-cy="matricule"
                type="text"
              />
              <ValidatedField
                label={translate('isimmManagerApp.professeur.departement')}
                id="professeur-departement"
                name="departement"
                data-cy="departement"
                type="select"
              >
                {departementValues.map(departement => (
                  <option value={departement} key={departement}>
                    {translate('isimmManagerApp.Departement.' + departement)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('isimmManagerApp.professeur.titre')}
                id="professeur-titre"
                name="titre"
                data-cy="titre"
                type="text"
              />
              <ValidatedField
                label={translate('isimmManagerApp.professeur.numTel')}
                id="professeur-numTel"
                name="numTel"
                data-cy="numTel"
                type="text"
              />
              <ValidatedField
                label={translate('isimmManagerApp.professeur.dateDeNaissance')}
                id="professeur-dateDeNaissance"
                name="dateDeNaissance"
                data-cy="dateDeNaissance"
                type="date"
              />
              <ValidatedBlobField
                label={translate('isimmManagerApp.professeur.photoDeProfile')}
                id="professeur-photoDeProfile"
                name="photoDeProfile"
                data-cy="photoDeProfile"
                isImage
                accept="image/*"
              />
              <ValidatedField
                label={translate('isimmManagerApp.professeur.matiere')}
                id="professeur-matiere"
                data-cy="matiere"
                type="select"
                multiple
                name="matieres"
              >
                <option value="" key="0" />
                {matieres
                  ? matieres.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/professeur" replace color="info">
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

export default ProfesseurUpdate;
