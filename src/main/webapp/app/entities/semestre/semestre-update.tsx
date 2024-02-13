import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { INiveau } from 'app/shared/model/niveau.model';
import { getEntities as getNiveaus } from 'app/entities/niveau/niveau.reducer';
import { IMatiere } from 'app/shared/model/matiere.model';
import { getEntities as getMatieres } from 'app/entities/matiere/matiere.reducer';
import { ISemestre } from 'app/shared/model/semestre.model';
import { getEntity, updateEntity, createEntity, reset } from './semestre.reducer';

export const SemestreUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const niveaus = useAppSelector(state => state.niveau.entities);
  const matieres = useAppSelector(state => state.matiere.entities);
  const semestreEntity = useAppSelector(state => state.semestre.entity);
  const loading = useAppSelector(state => state.semestre.loading);
  const updating = useAppSelector(state => state.semestre.updating);
  const updateSuccess = useAppSelector(state => state.semestre.updateSuccess);

  const handleClose = () => {
    navigate('/semestre');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getNiveaus({}));
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
    if (values.s !== undefined && typeof values.s !== 'number') {
      values.s = Number(values.s);
    }

    const entity = {
      ...semestreEntity,
      ...values,
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
          ...semestreEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="isimmManagerApp.semestre.home.createOrEditLabel" data-cy="SemestreCreateUpdateHeading">
            <Translate contentKey="isimmManagerApp.semestre.home.createOrEditLabel">Create or edit a Semestre</Translate>
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
                  id="semestre-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('isimmManagerApp.semestre.anneeScolaire')}
                id="semestre-anneeScolaire"
                name="anneeScolaire"
                data-cy="anneeScolaire"
                type="text"
              />
              <ValidatedField label={translate('isimmManagerApp.semestre.s')} id="semestre-s" name="s" data-cy="s" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/semestre" replace color="info">
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

export default SemestreUpdate;
