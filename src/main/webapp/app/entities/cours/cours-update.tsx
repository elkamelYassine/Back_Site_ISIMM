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
import { ICours } from 'app/shared/model/cours.model';
import { getEntity, updateEntity, createEntity, reset } from './cours.reducer';

export const CoursUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const matieres = useAppSelector(state => state.matiere.entities);
  const coursEntity = useAppSelector(state => state.cours.entity);
  const loading = useAppSelector(state => state.cours.loading);
  const updating = useAppSelector(state => state.cours.updating);
  const updateSuccess = useAppSelector(state => state.cours.updateSuccess);

  const handleClose = () => {
    navigate('/cours');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
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
      ...coursEntity,
      ...values,
      matiere: matieres.find(it => it.id.toString() === values.matiere.toString()),
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
          ...coursEntity,
          matiere: coursEntity?.matiere?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="isimmManagerApp.cours.home.createOrEditLabel" data-cy="CoursCreateUpdateHeading">
            <Translate contentKey="isimmManagerApp.cours.home.createOrEditLabel">Create or edit a Cours</Translate>
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
                  id="cours-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('isimmManagerApp.cours.titre')} id="cours-titre" name="titre" data-cy="titre" type="text" />
              <ValidatedBlobField
                label={translate('isimmManagerApp.cours.file')}
                id="cours-file"
                name="file"
                data-cy="file"
                openActionLabel={translate('entity.action.open')}
              />
              <ValidatedField
                id="cours-matiere"
                name="matiere"
                data-cy="matiere"
                label={translate('isimmManagerApp.cours.matiere')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/cours" replace color="info">
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

export default CoursUpdate;
