import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISemestre } from 'app/shared/model/semestre.model';
import { getEntities as getSemestres } from 'app/entities/semestre/semestre.reducer';
import { INiveau } from 'app/shared/model/niveau.model';
import { getEntity, updateEntity, createEntity, reset } from './niveau.reducer';

export const NiveauUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const semestres = useAppSelector(state => state.semestre.entities);
  const niveauEntity = useAppSelector(state => state.niveau.entity);
  const loading = useAppSelector(state => state.niveau.loading);
  const updating = useAppSelector(state => state.niveau.updating);
  const updateSuccess = useAppSelector(state => state.niveau.updateSuccess);

  const handleClose = () => {
    navigate('/niveau');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getSemestres({}));
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
      ...niveauEntity,
      ...values,
      semestre: semestres.find(it => it.id.toString() === values.semestre.toString()),
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
          ...niveauEntity,
          semestre: niveauEntity?.semestre?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="isimmManagerApp.niveau.home.createOrEditLabel" data-cy="NiveauCreateUpdateHeading">
            <Translate contentKey="isimmManagerApp.niveau.home.createOrEditLabel">Create or edit a Niveau</Translate>
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
                  id="niveau-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('isimmManagerApp.niveau.classe')}
                id="niveau-classe"
                name="classe"
                data-cy="classe"
                type="text"
              />
              <ValidatedField label={translate('isimmManagerApp.niveau.tp')} id="niveau-tp" name="tp" data-cy="tp" type="text" />
              <ValidatedField label={translate('isimmManagerApp.niveau.td')} id="niveau-td" name="td" data-cy="td" type="text" />
              <ValidatedField
                id="niveau-semestre"
                name="semestre"
                data-cy="semestre"
                label={translate('isimmManagerApp.niveau.semestre')}
                type="select"
              >
                <option value="" key="0" />
                {semestres
                  ? semestres.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/niveau" replace color="info">
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

export default NiveauUpdate;
