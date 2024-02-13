import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMatiere } from 'app/shared/model/matiere.model';
import { getEntities as getMatieres } from 'app/entities/matiere/matiere.reducer';
import { INiveau } from 'app/shared/model/niveau.model';
import { getEntities as getNiveaus } from 'app/entities/niveau/niveau.reducer';
import { ISeance } from 'app/shared/model/seance.model';
import { Jours } from 'app/shared/model/enumerations/jours.model';
import { Salle } from 'app/shared/model/enumerations/salle.model';
import { getEntity, updateEntity, createEntity, reset } from './seance.reducer';

export const SeanceUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const matieres = useAppSelector(state => state.matiere.entities);
  const niveaus = useAppSelector(state => state.niveau.entities);
  const seanceEntity = useAppSelector(state => state.seance.entity);
  const loading = useAppSelector(state => state.seance.loading);
  const updating = useAppSelector(state => state.seance.updating);
  const updateSuccess = useAppSelector(state => state.seance.updateSuccess);
  const joursValues = Object.keys(Jours);
  const salleValues = Object.keys(Salle);

  const handleClose = () => {
    navigate('/seance' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getMatieres({}));
    dispatch(getNiveaus({}));
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
    if (values.numSeance !== undefined && typeof values.numSeance !== 'number') {
      values.numSeance = Number(values.numSeance);
    }

    const entity = {
      ...seanceEntity,
      ...values,
      matiere: matieres.find(it => it.id.toString() === values.matiere.toString()),
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
          jour: 'Lundi',
          salle: 'A01',
          ...seanceEntity,
          matiere: seanceEntity?.matiere?.id,
          niveau: seanceEntity?.niveau?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="isimmManagerApp.seance.home.createOrEditLabel" data-cy="SeanceCreateUpdateHeading">
            <Translate contentKey="isimmManagerApp.seance.home.createOrEditLabel">Create or edit a Seance</Translate>
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
                  id="seance-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('isimmManagerApp.seance.jour')} id="seance-jour" name="jour" data-cy="jour" type="select">
                {joursValues.map(jours => (
                  <option value={jours} key={jours}>
                    {translate('isimmManagerApp.Jours.' + jours)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('isimmManagerApp.seance.numSeance')}
                id="seance-numSeance"
                name="numSeance"
                data-cy="numSeance"
                type="text"
                validate={{
                  min: { value: 1, message: translate('entity.validation.min', { min: 1 }) },
                  max: { value: 5, message: translate('entity.validation.max', { max: 5 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('isimmManagerApp.seance.salle')}
                id="seance-salle"
                name="salle"
                data-cy="salle"
                type="select"
              >
                {salleValues.map(salle => (
                  <option value={salle} key={salle}>
                    {translate('isimmManagerApp.Salle.' + salle)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="seance-matiere"
                name="matiere"
                data-cy="matiere"
                label={translate('isimmManagerApp.seance.matiere')}
                type="select"
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
              <ValidatedField
                id="seance-niveau"
                name="niveau"
                data-cy="niveau"
                label={translate('isimmManagerApp.seance.niveau')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/seance" replace color="info">
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

export default SeanceUpdate;
