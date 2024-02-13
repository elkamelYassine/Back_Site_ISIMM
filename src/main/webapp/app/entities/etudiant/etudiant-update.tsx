import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, UncontrolledTooltip } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { INiveau } from 'app/shared/model/niveau.model';
import { getEntities as getNiveaus } from 'app/entities/niveau/niveau.reducer';
import { IClub } from 'app/shared/model/club.model';
import { getEntities as getClubs } from 'app/entities/club/club.reducer';
import { IEtudiant } from 'app/shared/model/etudiant.model';
import { getEntity, updateEntity, createEntity, reset } from './etudiant.reducer';

export const EtudiantUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const niveaus = useAppSelector(state => state.niveau.entities);
  const clubs = useAppSelector(state => state.club.entities);
  const etudiantEntity = useAppSelector(state => state.etudiant.entity);
  const loading = useAppSelector(state => state.etudiant.loading);
  const updating = useAppSelector(state => state.etudiant.updating);
  const updateSuccess = useAppSelector(state => state.etudiant.updateSuccess);

  const handleClose = () => {
    navigate('/etudiant');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getNiveaus({}));
    dispatch(getClubs({}));
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
    if (values.numEtudiant !== undefined && typeof values.numEtudiant !== 'number') {
      values.numEtudiant = Number(values.numEtudiant);
    }

    const entity = {
      ...etudiantEntity,
      ...values,
      clubs: mapIdList(values.clubs),
      niveau: niveaus.find(it => it.id.toString() === values.niveau.toString()),
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
          ...etudiantEntity,
          niveau: etudiantEntity?.niveau?.id,
          clubs: etudiantEntity?.clubs?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="isimmManagerApp.etudiant.home.createOrEditLabel" data-cy="EtudiantCreateUpdateHeading">
            <Translate contentKey="isimmManagerApp.etudiant.home.createOrEditLabel">Create or edit a Etudiant</Translate>
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
                  id="etudiant-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('isimmManagerApp.etudiant.prenom')}
                id="etudiant-prenom"
                name="prenom"
                data-cy="prenom"
                type="text"
              />
              <UncontrolledTooltip target="prenomLabel">
                <Translate contentKey="isimmManagerApp.etudiant.help.prenom" />
              </UncontrolledTooltip>
              <ValidatedField label={translate('isimmManagerApp.etudiant.nom')} id="etudiant-nom" name="nom" data-cy="nom" type="text" />
              <ValidatedField
                label={translate('isimmManagerApp.etudiant.email')}
                id="etudiant-email"
                name="email"
                data-cy="email"
                type="text"
              />
              <ValidatedField
                label={translate('isimmManagerApp.etudiant.numEtudiant')}
                id="etudiant-numEtudiant"
                name="numEtudiant"
                data-cy="numEtudiant"
                type="text"
              />
              <ValidatedField
                label={translate('isimmManagerApp.etudiant.numTel')}
                id="etudiant-numTel"
                name="numTel"
                data-cy="numTel"
                type="text"
              />
              <ValidatedField
                label={translate('isimmManagerApp.etudiant.dateDeNaissance')}
                id="etudiant-dateDeNaissance"
                name="dateDeNaissance"
                data-cy="dateDeNaissance"
                type="date"
              />
              <ValidatedBlobField
                label={translate('isimmManagerApp.etudiant.photoDeProfile')}
                id="etudiant-photoDeProfile"
                name="photoDeProfile"
                data-cy="photoDeProfile"
                isImage
                accept="image/*"
              />
              <ValidatedField
                id="etudiant-niveau"
                name="niveau"
                data-cy="niveau"
                label={translate('isimmManagerApp.etudiant.niveau')}
                type="select"
              >
                <option value="" key="0" />
                {niveaus
                  ? niveaus.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('isimmManagerApp.etudiant.club')}
                id="etudiant-club"
                data-cy="club"
                type="select"
                multiple
                name="clubs"
              >
                <option value="" key="0" />
                {clubs
                  ? clubs.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/etudiant" replace color="info">
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

export default EtudiantUpdate;
